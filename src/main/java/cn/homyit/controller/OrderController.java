package cn.homyit.controller;

import cn.homyit.entity.DTO.OrderPageDto;
import cn.homyit.entity.VO.OrderPageVo;
import cn.homyit.entity.VO.PageVo;
import cn.homyit.entity.VO.Result;
import cn.homyit.enums.OrderStatusEnum;
import cn.homyit.log.SystemLog;
import cn.homyit.service.GoodsService;
import cn.homyit.service.OrderService;
import cn.homyit.service.UserService;
import cn.homyit.utils.UserUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.io.IOException;

/**
 * @program: graduate-website
 * @description:
 * @author: Charon
 * @create: 2023-03-28 18:34
 **/
@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private OrderService orderService;
    @Resource
    private UserService userService;
    @Resource
    private GoodsService goodsService;

    @PostMapping("/{goodsId}")
    @SystemLog(businessName = "尝试下单")
    public Result<Long> requestOrder(@PathVariable("goodsId") Long goodsId) {
        orderService.requestOrder(goodsId);
        return Result.success();
    }

    @PostMapping("/personOrderList")
    @SystemLog(businessName = "分页查询个人所有订单")
    public Result<?> personOrderList(@RequestBody @Validated OrderPageDto orderPageDto) {
        PageVo<?> orderPageVos = orderService.orderList(orderPageDto, UserUtils.getUser().getUser().getId());
        return Result.success(orderPageVos);
    }

    @GetMapping("/{id}")
    @SystemLog(businessName = "根据id查询订单信息")
    public Result<?> getOrderById(@PathVariable("id") Long id) {
        OrderPageVo orderPageVo = orderService.getOrderById(id);
        return Result.success(orderPageVo);
    }

    @GetMapping("/status")
    @SystemLog(businessName = "返回所有商品状态")
    public Result<?> getStatus() {
        return Result.success(OrderStatusEnum.values());
    }

    @GetMapping("/recognize-qr")
    @PreAuthorize("hasAuthority('system:admin')")
    @SystemLog(businessName = "识别二维码并返回该用户的所有订单")
    public Result<?> recognizeQr(@RequestParam @NotBlank String qrCode) {
        Long userId = userService.userUuidGetUserId(qrCode);
        // 根据id查询该用户的所有订单
        OrderPageDto orderPageDto = new OrderPageDto();
        orderPageDto.setStatus(String.valueOf(OrderStatusEnum.NOT_USE.getStatusCode()));
        orderPageDto.setPageNum(1);
        orderPageDto.setPageSize(100000);
        PageVo<?> orderPageVos = orderService.orderList(orderPageDto, userId);
        return Result.success(orderPageVos);
    }

    @PostMapping("/exchange/{id}")
    @PreAuthorize("hasAuthority('system:admin')")
    @SystemLog(businessName = "兑换商品")
    public Result<?> exchangeGoods(@PathVariable("id") Long id) {
        orderService.exchangeGoods(id);
        return Result.success();
    }

}
