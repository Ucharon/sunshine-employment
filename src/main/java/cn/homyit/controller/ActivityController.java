package cn.homyit.controller;

import cn.homyit.entity.DO.Activity;
import cn.homyit.entity.DO.ActivityTag;
import cn.homyit.entity.DTO.ActivityDto;
import cn.homyit.entity.DTO.ActivityPageDto;
import cn.homyit.entity.DTO.ClockInDto;
import cn.homyit.entity.VO.PageVo;
import cn.homyit.entity.VO.Result;
import cn.homyit.enums.ActivityOrderFieldEnum;
import cn.homyit.log.SystemLog;
import cn.homyit.service.ActivityService;
import cn.homyit.service.ActivityTagService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: graduate-website
 * @description:
 * @author: Charon
 * @create: 2023-03-30 12:58
 **/
@RestController
@RequestMapping("/activity")
public class ActivityController {

    @Resource
    private ActivityService activityService;
    @Resource
    private ActivityTagService activityTagService;

    @PostMapping("/list")
    @SystemLog(businessName = "分页查询活动信息")
    public Result<?> activityList(@RequestBody @Validated ActivityPageDto activityPageDto) {
        PageVo<?> activityPageVo = activityService.activityList(activityPageDto);
        return Result.success(activityPageVo);
    }

    @GetMapping("/orderFields")
    @SystemLog(businessName = "获取所有排序字段名称及其id")
    public Result<?> getOrderFields() {
        return Result.success(ActivityOrderFieldEnum.values());
    }

    @GetMapping("/tags")
    @SystemLog(businessName = "获取所有活动标签")
    public Result<List<ActivityTag>> tagList() {
        return Result.success(activityTagService.list());
    }

    @GetMapping("/{id}")
    @SystemLog(businessName = "根据id查询活动详情")
    public Result<?> getActivityById(@PathVariable("id") Long id) {
        Activity activity = activityService.getActivityById(id);
        return Result.success(activity);
    }

    @PostMapping("/sign/{id}")
    @SystemLog(businessName = "用户报名活动")
    public Result<?> signActivity(@PathVariable("id") Long id) {
         activityService.signActivity(id);
        return Result.success();
    }

    @PostMapping("/cancel/{id}")
    @SystemLog(businessName = "用户取消报名活动")
    public Result<?> cancelSign(@PathVariable("id") Long id) {
        activityService.cancelSign(id);
        return Result.success();
    }

    @PostMapping("/clockIn")
    @SystemLog(businessName = "用户打卡活动")
    public Result<?> clockInActivity(@RequestBody @Validated ClockInDto clockInDto) {
        activityService.clockInActivity(clockInDto);
        return Result.success();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('system:admin')")
    @SystemLog(businessName = "修改活动信息")
    public Result<?> updateActivity(@RequestBody @Validated ActivityDto activityDto, @PathVariable("id") Long id) {
        activityService.updateActivity(activityDto, id);
        return Result.success();
    }

    @PostMapping()
    @PreAuthorize("hasAuthority('system:admin')")
    @SystemLog(businessName = "添加活动")
    public Result<?> saveActivity(@RequestBody @Validated ActivityDto activityDto) {
        activityService.saveActivity(activityDto);
        return Result.success();
    }

    @DeleteMapping("/{ids}")
    @PreAuthorize("hasAuthority('system:admin')")
    @SystemLog(businessName = "下架活动")
    public Result<?> deleteActivity(@PathVariable("ids") List<Long> ids) {
        activityService.deleteActivity(ids);
        return Result.success();
    }


    

}
