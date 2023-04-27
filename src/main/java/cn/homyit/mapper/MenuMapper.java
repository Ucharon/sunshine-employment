package cn.homyit.mapper;

import cn.homyit.entity.DO.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author charon
* @description 针对表【sys_menu(权限表)】的数据库操作Mapper
* @createDate 2023-03-25 16:20:58
* @Entity cn.homyit.entity.DO.Menu
*/
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

}




