package cn.homyit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.homyit.entity.DO.ActivityTag;
import cn.homyit.service.ActivityTagService;
import cn.homyit.mapper.ActivityTagMapper;
import org.springframework.stereotype.Service;

/**
* @author charon
* @description 针对表【tb_activity_tag(活动标签表)】的数据库操作Service实现
* @createDate 2023-03-28 15:23:49
*/
@Service
public class ActivityTagServiceImpl extends ServiceImpl<ActivityTagMapper, ActivityTag>
    implements ActivityTagService{

}




