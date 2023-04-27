package cn.homyit.service.impl;

import cn.homyit.entity.DTO.RolePageDto;
import cn.homyit.entity.VO.PageVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.homyit.entity.DO.RoleModel;
import cn.homyit.service.RoleModelService;
import cn.homyit.mapper.RoleModelMapper;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
* @author charon
* @description 针对表【tb_role_model】的数据库操作Service实现
* @createDate 2023-04-07 09:52:18
*/
@Service
public class RoleModelServiceImpl extends ServiceImpl<RoleModelMapper, RoleModel>
    implements RoleModelService{

    @Override
    public PageVo<?> listRoleModel(RolePageDto roleModel) {
        Page<RoleModel> page = new Page<>(roleModel.getPageNum(), roleModel.getPageSize());
        lambdaQuery()
                .eq(Objects.nonNull(roleModel.getRoleModelTypeEnum()),
                        RoleModel::getRoleModelTypeEnum, roleModel.getRoleModelTypeEnum())
                .like(Objects.nonNull(roleModel.getName()),
                        RoleModel::getName, roleModel.getName())
                .like(Objects.nonNull(roleModel.getStuName()),
                        RoleModel::getStuName, roleModel.getStuName())
                .page(page);

        return new PageVo<>(page.getRecords(), page.getTotal());
    }
}




