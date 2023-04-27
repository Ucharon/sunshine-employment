package cn.homyit.service;

import cn.homyit.entity.DO.Goods;
import cn.homyit.entity.DTO.GoodsDto;
import cn.homyit.entity.DTO.GoodsPageDto;
import cn.homyit.entity.VO.PageVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author charon
* @description 针对表【tb_goods(商品表)】的数据库操作Service
* @createDate 2023-03-28 16:32:45
*/
public interface GoodsService extends IService<Goods> {

    PageVo<?> goodsList(GoodsPageDto goodsPageDto);

    Goods getGoods(Long id);

    void updateGoods(GoodsDto goodsDto, Long id);

    void saveGoods(GoodsDto goodsDto);

    void deleteGoodses(List<Long> ids);

}
