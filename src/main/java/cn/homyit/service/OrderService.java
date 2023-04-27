package cn.homyit.service;

import cn.homyit.entity.DO.Order;
import cn.homyit.entity.DTO.OrderPageDto;
import cn.homyit.entity.VO.OrderPageVo;
import cn.homyit.entity.VO.PageVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author charon
* @description 针对表【tb_order】的数据库操作Service
* @createDate 2023-03-28 18:33:44
*/
public interface OrderService extends IService<Order> {

    void requestOrder(Long goodsId);

    PageVo<?> orderList(OrderPageDto orderPageDto, Long userId);

    OrderPageVo getOrderById(Long id);

    void exchangeGoods(Long id);
}
