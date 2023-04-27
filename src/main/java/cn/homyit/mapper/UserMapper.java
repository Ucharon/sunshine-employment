package cn.homyit.mapper;

import cn.homyit.entity.DO.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author charon
* @description 针对表【sys_user(用户表)】的数据库操作Mapper
* @createDate 2023-03-25 17:21:07
* @Entity cn.homyit.entity.DO.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




