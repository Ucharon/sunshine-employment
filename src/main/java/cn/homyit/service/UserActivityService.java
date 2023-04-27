package cn.homyit.service;

import cn.homyit.entity.DO.UserActivity;
import cn.homyit.entity.DTO.ActivityClockInInfoPageDto;
import cn.homyit.entity.DTO.PageDto;
import cn.homyit.entity.DTO.UserActivityPageDto;
import cn.homyit.entity.VO.PageVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author charon
* @description 针对表【tb_user_activity(用户-活动关联表)】的数据库操作Service
* @createDate 2023-03-30 22:05:32
*/
public interface UserActivityService extends IService<UserActivity> {

    PageVo<UserActivity> userActivityList(UserActivityPageDto userActivityDto);

    PageVo<?> listClockInInfo(ActivityClockInInfoPageDto pageDto);
}
