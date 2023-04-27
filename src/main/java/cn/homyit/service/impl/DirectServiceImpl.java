package cn.homyit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.homyit.entity.DO.Direct;
import cn.homyit.service.DirectService;
import cn.homyit.mapper.DirectMapper;
import org.springframework.stereotype.Service;

/**
* @author charon
* @description 针对表【tb_direct(方向表)】的数据库操作Service实现
* @createDate 2023-03-26 16:29:16
*/
@Service
public class DirectServiceImpl extends ServiceImpl<DirectMapper, Direct>
    implements DirectService{

}




