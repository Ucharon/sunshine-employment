package cn.homyit.mapper;

import cn.homyit.entity.DO.Goods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author charon
* @description 针对表【tb_goods(商品表)】的数据库操作Mapper
* @createDate 2023-03-28 16:32:45
* @Entity cn.homyit.entity.DO.Goods
*/
@Mapper
public interface GoodsMapper extends BaseMapper<Goods> {

}




