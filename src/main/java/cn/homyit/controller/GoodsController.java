package cn.homyit.controller;

import cn.homyit.entity.DO.Goods;
import cn.homyit.entity.DTO.GoodsDto;
import cn.homyit.entity.DTO.GoodsPageDto;
import cn.homyit.entity.VO.PageVo;
import cn.homyit.entity.VO.Result;
import cn.homyit.log.SystemLog;
import cn.homyit.service.GoodsService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: graduate-website
 * @description:
 * @author: Charon
 * @create: 2023-03-28 16:33
 **/

@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Resource
    private GoodsService goodsService;

    @PostMapping("/list")
    @SystemLog(businessName = "商品信息分页查询")
    public Result<?> goodsList(@RequestBody @Validated GoodsPageDto goodsPageDto) {
        PageVo<?> goodsPageVo = goodsService.goodsList(goodsPageDto);
        return Result.success(goodsPageVo);
    }

    @GetMapping("/{id}")
    @SystemLog(businessName = "获取商品详细信息")
    public Result<Goods> getGoods(@PathVariable("id") Long id) {
        Goods goods = goodsService.getGoods(id);
        return Result.success(goods);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('system:admin')")
    @SystemLog(businessName = "修改商品信息")
    public Result<?> updateGoods(@RequestBody @Validated GoodsDto goodsDto, @PathVariable("id") Long id) {
        goodsService.updateGoods(goodsDto, id);
        return Result.success();
    }

    @PostMapping()
    @PreAuthorize("hasAuthority('system:admin')")
    @SystemLog(businessName = "添加商品")
    public Result<?> saveGoods(@RequestBody @Validated GoodsDto goodsDto) {
        goodsService.saveGoods(goodsDto);
        return Result.success();
    }

    @DeleteMapping("/{ids}")
    @PreAuthorize("hasAuthority('system:admin')")
    @SystemLog(businessName = "下架商品")
    public Result<?> deleteGoodses(@PathVariable("ids") List<Long> ids) {
        goodsService.deleteGoodses(ids);
        return Result.success();
    }
}
