package cn.homyit.controller;

import cn.homyit.entity.DO.Appoint;
import cn.homyit.entity.DTO.AppointPageDto;
import cn.homyit.entity.VO.PageVo;
import cn.homyit.entity.VO.Result;
import cn.homyit.enums.AppointStatusEnum;
import cn.homyit.enums.AppointTypeEnum;
import cn.homyit.log.SystemLog;
import cn.homyit.service.AppointService;
import cn.homyit.utils.UserUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @program: graduate-website
 * @description:
 * @author: Charon
 * @create: 2023-04-06 13:20
 **/
@RestController
@RequestMapping("/appoint")
public class AppointController {

    @Resource
    private AppointService appointService;

    @PostMapping()
    @SystemLog(businessName = "学生发起预约请求")
    public Result<?> saveAppoint(@RequestBody @Validated Appoint appoint) {
        appointService.saveAppoint(appoint);
        return Result.success();
    }

    @GetMapping("/statusEnums")
    @SystemLog(businessName = "获取所有预约状态")
    public Result<?> statusEnumList() {
        return Result.success(AppointStatusEnum.values());
    }

    @GetMapping("/typeEnums")
    @SystemLog(businessName = "获取所有预约类型")
    public Result<?> typeEnumList() {
        return Result.success(AppointTypeEnum.values());
    }

    @PostMapping("/student/list")
    @SystemLog(businessName = "学生分页查询个人预约信息")
    public Result<?> stuAppointList(@RequestBody @Validated AppointPageDto appointPageDto) {
        appointPageDto.setStuId(UserUtils.getUser().getUser().getId());
        PageVo<?> pageVo = appointService.appointList(appointPageDto);
        return Result.success(pageVo);
    }

    @PostMapping("/teacher/list")
    @PreAuthorize("hasAuthority('system:teacher')")
    @SystemLog(businessName = "老师分页查询个人预约信息")
    public Result<?> teaAppointList(@RequestBody @Validated AppointPageDto appointPageDto) {
        appointPageDto.setTeaId(UserUtils.getUser().getUser().getId());
        PageVo<?> pageVo = appointService.appointList(appointPageDto);
        return Result.success(pageVo);
    }

    @PostMapping("/teacher/agree")
    @PreAuthorize("hasAuthority('system:teacher')")
    @SystemLog(businessName = "老师同意预约")
    public Result<?> teacherAgreeAppoint(@RequestBody @Validated Appoint appoint) {
        appoint.setAppointStatusEnum(AppointStatusEnum.TEACHER_AGREES);
        appointService.teacherUpdateAppoint(appoint);
        return Result.success();
    }

    @PostMapping("/teacher/reject")
    @PreAuthorize("hasAuthority('system:teacher')")
    @SystemLog(businessName = "老师拒绝预约")
    public Result<?> teacherRejectAppoint(@RequestBody @Validated Appoint appoint) {
        appoint.setAppointStatusEnum(AppointStatusEnum.TEACHER_REJECT);
        appointService.teacherUpdateAppoint(appoint);
        return Result.success();
    }

    @PostMapping("/teacher/finish")
    @PreAuthorize("hasAuthority('system:teacher')")
    @SystemLog(businessName = "老师完成预约")
    public Result<?> teacherFinishAppoint(@RequestBody @Validated Appoint appoint) {
        appoint.setAppointStatusEnum(AppointStatusEnum.END_OF_APPOINTMENT);
        appointService.teacherUpdateAppoint(appoint);
        return Result.success();
    }

}
