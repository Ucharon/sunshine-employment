package cn.homyit.mapper;

import cn.homyit.entity.DO.RoleMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author charon
* @description 针对表【sys_role_menu(角色权限关系表)】的数据库操作Mapper
* @createDate 2023-03-25 16:20:59
* @Entity cn.homyit.entity.DO.RoleMenu
*/
@Mapper
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

}




