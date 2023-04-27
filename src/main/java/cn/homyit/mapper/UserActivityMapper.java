package cn.homyit.mapper;

import cn.homyit.entity.DO.UserActivity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author charon
* @description 针对表【tb_user_activity(用户-活动关联表)】的数据库操作Mapper
* @createDate 2023-03-30 22:05:32
* @Entity cn.homyit.entity.DO.UserActivity
*/
@Mapper
public interface UserActivityMapper extends BaseMapper<UserActivity> {

}




