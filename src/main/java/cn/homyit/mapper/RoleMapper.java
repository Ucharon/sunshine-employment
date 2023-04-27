package cn.homyit.mapper;

import cn.homyit.entity.DO.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author charon
* @description 针对表【sys_role(角色表)】的数据库操作Mapper
* @createDate 2023-03-25 16:20:58
* @Entity cn.homyit.entity.DO.Role
*/
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    List<String> selectPerms(Integer roleId);
}




