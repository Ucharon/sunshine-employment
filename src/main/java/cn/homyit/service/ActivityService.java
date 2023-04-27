package cn.homyit.service;

import cn.homyit.entity.DO.Activity;
import cn.homyit.entity.DTO.ActivityDto;
import cn.homyit.entity.DTO.ActivityPageDto;
import cn.homyit.entity.DTO.ClockInDto;
import cn.homyit.entity.VO.PageVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author charon
* @description 针对表【tb_activity(活动表)】的数据库操作Service
* @createDate 2023-03-28 15:23:49
*/
public interface ActivityService extends IService<Activity> {

    PageVo<?> activityList(ActivityPageDto activityPageDto);

    Activity getActivityById(Long id);

    void signActivity(Long id);

    void updateActivity(ActivityDto activityDto, Long id);

    void saveActivity(ActivityDto activityDto);

    void deleteActivity(List<Long> ids);

    void cancelSign(Long id);

    void clockInActivity(ClockInDto clockInDto);

    List<Long> getActivityIdsByName(String activityName);
}
