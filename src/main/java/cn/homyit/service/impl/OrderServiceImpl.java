package cn.homyit.service.impl;

import cn.homyit.entity.DO.Goods;
import cn.homyit.entity.DO.Order;
import cn.homyit.entity.DO.User;
import cn.homyit.entity.DTO.OrderPageDto;
import cn.homyit.entity.VO.OrderPageVo;
import cn.homyit.entity.VO.PageVo;
import cn.homyit.enums.OrderStatusEnum;
import cn.homyit.enums.ResultCodeEnum;
import cn.homyit.enums.ScoreRecordTypesEnum;
import cn.homyit.exception.BizException;
import cn.homyit.mapper.OrderMapper;
import cn.homyit.service.GoodsService;
import cn.homyit.service.OrderService;
import cn.homyit.service.ScoreRecordsService;
import cn.homyit.service.UserService;
import cn.homyit.utils.BeanCopyUtils;
import cn.homyit.utils.RedisCache;
import cn.homyit.utils.TimeUtils;
import cn.homyit.utils.UserUtils;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static cn.homyit.constant.RedisConstants.GOODS_CACHE_KEY;
import static cn.homyit.constant.RedisConstants.USER_CACHE_KEY;

/**
 * @author charon
 * @description 针对表【tb_order】的数据库操作Service实现
 * @createDate 2023-03-28 18:33:44
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order>
        implements OrderService {

    @Resource
    private RedisCache redisCache;
    @Resource
    private GoodsService goodsService;
    @Resource
    private UserService userService;
    @Resource
    private ScoreRecordsService scoreRecordsService;

    @Override
    @Transactional
    public void requestOrder(Long goodsId) {
        // 并没有解决并发安全问题！（已解决，详见73行，原来用的userScore并不是最新值）

        //前提：没有一人一单的需求
        //这里使用乐观锁解决超卖问题（简单的秒杀应用）
        User user = UserUtils.getUser().getUser();
        Goods goods = Optional.ofNullable(goodsService.getGoods(goodsId))
                .orElseThrow(() -> new BizException(ResultCodeEnum.GOODS_NOT_EXIST));

        //1. 判断用户当前的积分是否能够购买商品
        //1.1 获取当前用户的积分
        Long userScore = userService.getById(user.getId()).getScore();
        //1.2 与商品所需的积分进行比较
        if (goods.getScore() > userScore) {
            throw new BizException(ResultCodeEnum.INSUFFICIENT_SCORE);
        }
        //2. 走到这里表示用户积分足够，开始判断库存是否足够
        if (goods.getStock() <= 0) {
            //2.1 库存小于等于0，表示以及卖完
            throw new BizException(ResultCodeEnum.INSUFFICIENT_INVENTORY);
        }
        //3. 开始扣减用户积分和库存
        //3.1 扣除库存
        boolean success = goodsService.lambdaUpdate()
                .setSql("stock = stock - 1")
                .eq(Goods::getId, goodsId)
                //更新的同时需检测库存是否大于0
                .gt(Goods::getStock, 0)
                .update();
        //3.2 检测扣除库存是否成功
        if (!success) {
            //3.3 失败情况
            throw new BizException(ResultCodeEnum.INSUFFICIENT_INVENTORY);
        }
        //3.4 开始扣除积分
        success = userService.lambdaUpdate()
                .setSql("score = score - " + goods.getScore())
                //.set(User::getScore, userScore - goods.getScore())
                .eq(User::getId, user.getId())
                //同时检测当前积分是否大于等于商品积分
                .ge(User::getScore, goods.getScore())
                .update();
        //3.3 检测扣除积分是否成功
        if (!success) {
            throw new BizException(ResultCodeEnum.INSUFFICIENT_SCORE);
        }

        //4. 走到这里表示用户以及购买到商品了！接下来生成订单
        Order order = new Order();
        order.setUserId(user.getId());
        order.setGoodsId(goodsId);
        order.setStatusEnum(OrderStatusEnum.NOT_USE);
        save(order);

        //这里删除redis缓存的商品信息和用户个人信息
        redisCache.deleteObject(USER_CACHE_KEY + user.getId());
        redisCache.deleteObject(GOODS_CACHE_KEY + goodsId);

        //记录积分流水
        scoreRecordsService.insertRecord(userScore, -goods.getScore(), order.getId(), ScoreRecordTypesEnum.BUY_GOODS);
    }

    @Override
    public PageVo<?> orderList(OrderPageDto orderPageDto, Long userId) {
        String goodsName = orderPageDto.getGoodsName();
        List<Long> goodsIds = null;
        //校验模糊检索字段是否为空
        if (StrUtil.isNotBlank(goodsName)) {
            //不为空，则开始模糊检索出所有的goodsId
            goodsIds = goodsService.lambdaQuery()
                    .like(Goods::getName, goodsName)
                    .select(Goods::getId)
                    .list().stream()
                    .map(Goods::getId).collect(Collectors.toList());
        }

        //开始查询订单
        Page<Order> page = new Page<>(orderPageDto.getPageNum(), orderPageDto.getPageSize());
        lambdaQuery()
                //筛选当前用户的订单
                .eq(Order::getUserId, userId)
                //筛选订单的商品id
                .in(CollectionUtil.isNotEmpty(goodsIds), Order::getGoodsId, goodsIds)
                //根据订单状态查询
                .eq(Objects.nonNull(orderPageDto.getStatus()), Order::getStatusEnum, orderPageDto.getStatus())
                //根据时间范围查询
                .ge(Objects.nonNull(orderPageDto.getBeginTime()), Order::getCreateTime, orderPageDto.getBeginTime())
                .le(Objects.nonNull(orderPageDto.getEndTime()), Order::getCreateTime, orderPageDto.getEndTime())
                .orderByDesc(Order::getCreateTime)
                .page(page);
        List<Order> orderList = page.getRecords();
        List<OrderPageVo> orderPageVos = BeanCopyUtils.copyBeanList(orderList, OrderPageVo.class);
        //封装order中的商品简要信息
        orderPageVos = orderPageVos.stream().peek(orderPageVo -> {
            //先封装其订单状态
            Long goodsId = orderPageVo.getGoodsId();
            Goods goods = Optional.ofNullable(goodsService.getGoods(goodsId))
                    .orElse(Goods.getDisableGoods());
            orderPageVo.setGoodsName(goods.getName());
            orderPageVo.setThumbnail(goods.getThumbnail());
        }).collect(Collectors.toList());

        //封装到pageVo并返回
        return new PageVo<>(orderPageVos, page.getTotal());
    }

    @Override
    public OrderPageVo getOrderById(Long id) {
        Optional<Order> orderOptional = Optional.ofNullable(getById(id));
        Order order = orderOptional.orElseThrow(() -> new BizException(ResultCodeEnum.ORDER_NOT_EXIST));
        OrderPageVo orderPageVo = BeanCopyUtils.copyBean(order, OrderPageVo.class);
        Goods goods = goodsService.getById(order.getGoodsId());
        orderPageVo.setGoodsName(goods.getName());
        orderPageVo.setThumbnail(goods.getThumbnail());
        return orderPageVo;
    }

    @Override
    public void exchangeGoods(Long id) {
        //开始兑换商品
        lambdaUpdate()
                .eq(Order::getId, id)
                .set(Order::getUsedTime, TimeUtils.getCurrentTime())
                .set(Order::getStatusEnum, OrderStatusEnum.ALREADY_US)
                .update();

    }
}




