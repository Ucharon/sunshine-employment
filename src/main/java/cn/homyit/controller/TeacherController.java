package cn.homyit.controller;

import cn.homyit.entity.DTO.ActivityClockInInfoPageDto;
import cn.homyit.entity.DTO.ActivityPageDto;
import cn.homyit.entity.VO.PageVo;
import cn.homyit.entity.VO.Result;
import cn.homyit.log.SystemLog;
import cn.homyit.service.ActivityService;
import cn.homyit.service.ScoreRecordsService;
import cn.homyit.service.UserActivityService;
import cn.homyit.service.UserService;
import cn.homyit.utils.UserUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @program: graduate-website
 * @description:
 * @author: Charon
 * @create: 2023-04-05 16:25
 **/
@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Resource
    private UserService userService;
    @Resource
    private ActivityService activityService;
    @Resource
    private UserActivityService userActivityService;
    @Resource
    private ScoreRecordsService scoreRecordsService;


    @PostMapping("/activity/list")
    @PreAuthorize("hasAuthority('system:teacher')")
    @SystemLog(businessName = "分页查询老师个人发布的活动")
    public Result<?> listActivities(@RequestBody @Validated ActivityPageDto activityPageDto) {
        activityPageDto.setUserId(UserUtils.getUser().getUser().getId());
        PageVo<?> activityList = activityService.activityList(activityPageDto);
        return Result.success(activityList);
    }

    @PostMapping("/activity/clockInInfo")
    @PreAuthorize("hasAuthority('system:teacher')")
    @SystemLog(businessName = "老师查看活动打卡情况")
    public Result<?> listClockInInfo(@RequestBody @Validated ActivityClockInInfoPageDto pageDto) {
        PageVo<?> pageVo = userActivityService.listClockInInfo(pageDto);
        return Result.success(pageVo);
    }

    //@PostMapping("/disqualifyUser/{userActivityId}")
    //@PreAuthorize("hasAuthority('system:teacher')")
    //@SystemLog(businessName = "取消学生参与资格")
    //public Result<?> disqualifyUser(@PathVariable("userActivityId") Long userActivityId) {
    //    UserActivity userActivity = userActivityService.getById(userActivityId);
    //    //取消活动记录
    //    userActivityService.lambdaUpdate()
    //            .eq(UserActivity::getId, userActivityId)
    //            .set(UserActivity::getStatus, 1)
    //            .set(UserActivity::getUpdateTime, TimeUtils.getCurrentTime())
    //            .update();
    //    //查询出该活动的积分
    //    Long score = activityService.getActivityById(userActivity.getActivityId()).getScore();
    //    //查询原积分
    //    User user = userService.getById(userActivity.getUserId());
    //    //扣减该学生的积分
    //    userService.lambdaUpdate()
    //            .eq(User::getId, userActivity.getUserId())
    //            .setSql("score = score - " + score)
    //            .update();
    //    //增加积分流水
    //    scoreRecordsService.insertRecord(user.getScore(), -score, userActivityId,
    //            DISQUALIFICATION_ACTIVITY, user.getId());
    //    return Result.success();
    //}


}
