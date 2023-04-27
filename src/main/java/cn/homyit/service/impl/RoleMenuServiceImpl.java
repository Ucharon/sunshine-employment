package cn.homyit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.homyit.entity.DO.RoleMenu;
import cn.homyit.service.RoleMenuService;
import cn.homyit.mapper.RoleMenuMapper;
import org.springframework.stereotype.Service;

/**
* @author charon
* @description 针对表【sys_role_menu(角色权限关系表)】的数据库操作Service实现
* @createDate 2023-03-25 16:20:59
*/
@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu>
    implements RoleMenuService{

}




