package cn.homyit.service.impl;

import cn.homyit.entity.DTO.GoodsDto;
import cn.homyit.entity.DTO.GoodsPageDto;
import cn.homyit.entity.VO.PageVo;
import cn.homyit.utils.BeanCopyUtils;
import cn.homyit.utils.CacheClient;
import cn.homyit.utils.RedisCache;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.homyit.entity.DO.Goods;
import cn.homyit.service.GoodsService;
import cn.homyit.mapper.GoodsMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static cn.homyit.constant.CommonConstants.GOODS_DISABLE;
import static cn.homyit.constant.CommonConstants.GOODS_ENABLE;
import static cn.homyit.constant.RedisConstants.*;

/**
 * @author charon
 * @description 针对表【tb_goods(商品表)】的数据库操作Service实现
 * @createDate 2023-03-28 16:32:45
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods>
        implements GoodsService {

    @Resource
    private CacheClient cacheClient;
    @Resource
    private RedisCache redisCache;

    @Override
    public PageVo<?> goodsList(GoodsPageDto goodsPageDto) {
        Page<Goods> page = new Page<>(goodsPageDto.getPageNum(), goodsPageDto.getPageSize());
        lambdaQuery()
                .eq(Goods::getStatus, GOODS_ENABLE)
                .like(Objects.nonNull(goodsPageDto.getName()), Goods::getName, goodsPageDto.getName())
                .orderByDesc(Goods::getUpdateTime)
                .select(Goods::getId, Goods::getName, Goods::getScore,
                        Goods::getStock, Goods::getThumbnail, Goods::getCreateTime)
                .page(page);
        List<Goods> goods = page.getRecords();

        return new PageVo<>(goods, page.getTotal());
    }

    @Override
    public Goods getGoods(Long id) {
        return cacheClient.queryWithMutex(GOODS_CACHE_KEY, GOODS_LOCK_KEY,
                id, Goods.class, this::getGoodsByMapper,
                GOODS_CACHE_TTL, TimeUnit.HOURS);
    }

    public Goods getGoodsByMapper(Long id) {
        return lambdaQuery().eq(Goods::getStatus, GOODS_ENABLE).eq(Goods::getId, id).one();
    }

    @Override
    public void updateGoods(GoodsDto goodsDto, Long id) {
        Goods goods = BeanCopyUtils.copyBean(goodsDto, Goods.class);
        goods.setId(id);
        updateById(goods);
        //删除缓存
        redisCache.deleteObject(GOODS_CACHE_KEY + id.toString());
    }

    @Override
    public void saveGoods(GoodsDto goodsDto) {
        Goods goods = BeanCopyUtils.copyBean(goodsDto, Goods.class);
        save(goods);
        //删除空缓存
        redisCache.deleteObject(GOODS_CACHE_KEY + goods.getId().toString());
    }

    @Override
    public void deleteGoodses(List<Long> ids) {
        lambdaUpdate().in(!ids.isEmpty(), Goods::getId, ids)
                .set(Goods::getStatus, GOODS_DISABLE)
                .update();
    }

}




