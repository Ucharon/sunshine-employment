package cn.homyit.controller;

import cn.homyit.entity.DO.DailyClockIn;
import cn.homyit.entity.DTO.DailyClockInPageDto;
import cn.homyit.entity.VO.PageVo;
import cn.homyit.entity.VO.Result;
import cn.homyit.log.SystemLog;
import cn.homyit.service.DailyClockInService;
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
 * @create: 2023-04-08 14:46
 **/
@RestController
@RequestMapping("/dailyClockIn")
public class DailyClockInController {

    @Resource
    private DailyClockInService dailyClockInService;

    @PostMapping("/list")
    @PreAuthorize("hasAuthority('system:admin')")
    @SystemLog(businessName = "分页查询学生每日打卡信息")
    public Result<?> listDailyClockIn(@RequestBody @Validated DailyClockInPageDto pageDto) {
        PageVo<?> pageVo = dailyClockInService.listDailyClockIn(pageDto);
        return Result.success(pageVo);
    }




}
