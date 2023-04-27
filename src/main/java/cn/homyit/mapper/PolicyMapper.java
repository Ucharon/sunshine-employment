package cn.homyit.mapper;

import cn.homyit.entity.DO.Policy;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author charon
* @description 针对表【tb_policy】的数据库操作Mapper
* @createDate 2023-04-08 12:59:29
* @Entity cn.homyit.entity.DO.Policy
*/
@Mapper
public interface PolicyMapper extends BaseMapper<Policy> {

}




