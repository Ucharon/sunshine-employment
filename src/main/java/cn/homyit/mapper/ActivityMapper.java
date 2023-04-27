package cn.homyit.mapper;

import cn.homyit.entity.DO.Activity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author charon
* @description 针对表【tb_activity(活动表)】的数据库操作Mapper
* @createDate 2023-03-28 15:23:49
* @Entity cn.homyit.entity.DO.Activity
*/
@Mapper
public interface ActivityMapper extends BaseMapper<Activity> {

}




