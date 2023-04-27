package cn.homyit.controller;

import cn.homyit.entity.DO.Policy;
import cn.homyit.entity.DTO.PolicyPageDto;
import cn.homyit.entity.VO.PageVo;
import cn.homyit.entity.VO.Result;
import cn.homyit.log.SystemLog;
import cn.homyit.service.PolicyService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: graduate-website
 * @description:
 * @author: Charon
 * @create: 2023-04-08 13:00
 **/
@RestController
@RequestMapping("/policy")
public class PolicyController {

    @Resource
    private PolicyService policyService;

    @PostMapping()
    @PreAuthorize("hasAuthority('system:admin')")
    @SystemLog(businessName = "添加政策信息")
    public Result<?> savePolicy(@RequestBody @Validated Policy policy) {
        policyService.save(policy);
        return Result.success();
    }

    @DeleteMapping("/{ids}")
    @PreAuthorize("hasAuthority('system:admin')")
    @SystemLog(businessName = "删除政策信息")
    public Result<?> removePolicy(@PathVariable List<Integer> ids) {
        policyService.removeBatchByIds(ids);
        return Result.success();
    }

    @PutMapping()
    @PreAuthorize("hasAuthority('system:admin')")
    @SystemLog(businessName = "修改政策信息")
    public Result<?> updatePolicy(@RequestBody @Validated Policy policy) {
        policyService.updateById(policy);
        return Result.success();
    }

    @PostMapping("/list")
    @SystemLog(businessName = "分页查询政策信息")
    public Result<?> listPolicy(@RequestBody @Validated PolicyPageDto policyPageDto) {
        PageVo<?> pageVo = policyService.listPolicy(policyPageDto);
        return Result.success(pageVo);
    }

    @GetMapping("/{id}")
    @SystemLog(businessName = "查看政策的详细信息")
    public Result<?> getPolicyById(@PathVariable Integer id) {
        Policy policy = policyService.getById(id);
        return Result.success(policy);
    }

}
