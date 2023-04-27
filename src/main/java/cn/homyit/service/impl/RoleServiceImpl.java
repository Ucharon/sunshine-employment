package cn.homyit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.homyit.entity.DO.Role;
import cn.homyit.service.RoleService;
import cn.homyit.mapper.RoleMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author charon
* @description 针对表【sys_role(角色表)】的数据库操作Service实现
* @createDate 2023-03-25 16:20:58
*/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
    implements RoleService{


}




