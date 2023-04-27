package cn.homyit.controller;

import cn.homyit.entity.VO.Result;
import cn.homyit.log.SystemLog;
import cn.homyit.service.ExcelService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @program: graduate-website
 * @description:
 * @author: Charon
 * @create: 2023-04-02 17:46
 **/
@RestController
@RequestMapping("/excel")
public class ExcelController {

    @Resource
    private ExcelService excelService;

    @PostMapping("/upload-user")
    @PreAuthorize("hasAuthority('system:admin')")
    @SystemLog(businessName = "excel上传用户信息")
    public Result<?> uploadUsers(@RequestParam("file") MultipartFile file) {
        excelService.uploadUsers(file);
        return Result.success();
    }

}
