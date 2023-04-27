package cn.homyit.controller;

import cn.homyit.entity.DO.RoleModel;
import cn.homyit.entity.DTO.RolePageDto;
import cn.homyit.entity.VO.PageVo;
import cn.homyit.entity.VO.Result;
import cn.homyit.enums.RoleModelTypeEnum;
import cn.homyit.log.SystemLog;
import cn.homyit.service.RoleModelService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: graduate-website
 * @description:
 * @author: Charon
 * @create: 2023-04-07 09:53
 **/
@RestController
@RequestMapping("/model")
public class RoleModelController {

    @Resource
    private RoleModelService roleModelService;

    @PostMapping("/list")
    @SystemLog(businessName = "分页查询榜样")
    public Result<?> listRoleModel(@RequestBody @Validated RolePageDto roleModel) {
        PageVo<?> pageVo = roleModelService.listRoleModel(roleModel);
        return Result.success(pageVo);
    }

    @GetMapping("/typeEnum")
    @SystemLog(businessName = "获取所有榜样类型枚举")
    public Result<?> getTypeEnum() {
        RoleModelTypeEnum[] values = RoleModelTypeEnum.values();
        return Result.success(values);
    }

    @PostMapping()
    @PreAuthorize("hasAuthority('system:admin')")
    @SystemLog(businessName = "添加新榜样")
    public Result<?> saveRoleModel(@RequestBody @Validated RoleModel roleModel) {
        roleModelService.save(roleModel);
        return Result.success();
    }

    @PutMapping()
    @PreAuthorize("hasAuthority('system:admin')")
    @SystemLog(businessName = "修改榜样")
    public Result<?> updateRoleModel(@RequestBody @Validated RoleModel roleModel) {
        roleModelService.updateById(roleModel);
        return Result.success();
    }

    @DeleteMapping("/{ids}")
    @PreAuthorize("hasAuthority('system:admin')")
    @SystemLog(businessName = "删除榜样")
    public Result<?> deleteRoleModel(@PathVariable("ids") List<Long> ids) {
        roleModelService.removeBatchByIds(ids);
        return Result.success();
    }

    @GetMapping("/{id}")
    @SystemLog(businessName = "查询榜样的详细信息")
    public Result<?> getModelById(@PathVariable Long id) {
        RoleModel roleModel = roleModelService.getById(id);
        return Result.success(roleModel);
    }


}
