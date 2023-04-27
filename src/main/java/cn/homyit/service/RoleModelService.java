package cn.homyit.service;

import cn.homyit.entity.DO.RoleModel;
import cn.homyit.entity.DTO.RolePageDto;
import cn.homyit.entity.VO.PageVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author charon
* @description 针对表【tb_role_model】的数据库操作Service
* @createDate 2023-04-07 09:52:18
*/
public interface RoleModelService extends IService<RoleModel> {

    PageVo<?> listRoleModel(RolePageDto roleModel);

}
