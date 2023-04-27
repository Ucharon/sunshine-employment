package cn.homyit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.homyit.entity.DO.Menu;
import cn.homyit.service.MenuService;
import cn.homyit.mapper.MenuMapper;
import org.springframework.stereotype.Service;

/**
* @author charon
* @description 针对表【sys_menu(权限表)】的数据库操作Service实现
* @createDate 2023-03-25 16:20:58
*/
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu>
    implements MenuService{

}




