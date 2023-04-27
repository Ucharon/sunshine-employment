package cn.homyit.controller;

import cn.homyit.entity.VO.Result;
import cn.homyit.log.SystemLog;
import cn.homyit.service.DirectService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @program: graduate-website
 * @description:
 * @author: Charon
 * @create: 2023-03-30 20:01
 **/
@RestController
@RequestMapping("/direct")
public class DirectController {

    @Resource
    private DirectService directService;

    @GetMapping("/list")
    @SystemLog(businessName = "查询所有方向及其对应的id")
    public Result<?> directList() {
        return Result.success(directService.list());
    }

}
