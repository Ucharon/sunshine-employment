package cn.homyit.mapper;

import cn.homyit.entity.DO.Appoint;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author charon
* @description 针对表【tb_appoint(预约表)】的数据库操作Mapper
* @createDate 2023-04-05 23:36:38
* @Entity cn.homyit.entity.DO.Appoint
*/
@Mapper
public interface AppointMapper extends BaseMapper<Appoint> {

}




