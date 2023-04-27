package cn.homyit.mapper;

import cn.homyit.entity.DO.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author charon
* @description 针对表【tb_order】的数据库操作Mapper
* @createDate 2023-03-28 18:33:44
* @Entity cn.homyit.entity.DO.Order
*/
@Mapper
public interface OrderMapper extends BaseMapper<Order> {

}




