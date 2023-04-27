package cn.homyit.service.impl;

import cn.homyit.entity.DO.Activity;
import cn.homyit.entity.DO.User;
import cn.homyit.entity.DTO.ActivityClockInInfoPageDto;
import cn.homyit.entity.DTO.PageDto;
import cn.homyit.entity.DTO.UserActivityPageDto;
import cn.homyit.entity.VO.PageVo;
import cn.homyit.mapper.ActivityMapper;
import cn.homyit.mapper.UserMapper;
import cn.homyit.utils.UserUtils;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.homyit.entity.DO.UserActivity;
import cn.homyit.service.UserActivityService;
import cn.homyit.mapper.UserActivityMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author charon
 * @description 针对表【tb_user_activity(用户-活动关联表)】的数据库操作Service实现
 * @createDate 2023-03-30 22:05:32
 */
@Service
public class UserActivityServiceImpl extends ServiceImpl<UserActivityMapper, UserActivity>
        implements UserActivityService {

    @Resource
    private ActivityMapper activityMapper;
    @Resource
    private UserMapper userMapper;

    @Override
    public PageVo<UserActivity> userActivityList(UserActivityPageDto userActivityDto) {
        Page<UserActivity> page = new Page<>(userActivityDto.getPageNum(), userActivityDto.getPageSize());
        lambdaQuery()
                //过滤用户id
                .eq(UserActivity::getUserId, UserUtils.getUser().getUser().getId())
                //如果填写了模糊查询活动字段，则过滤下结果
                .in(Objects.nonNull(userActivityDto.getActivityIds()),
                        UserActivity::getActivityId, userActivityDto.getActivityIds())
                //过滤活动参与状态
                .eq(Objects.nonNull(userActivityDto.getIsComplete()),
                        UserActivity::getIsComplete, userActivityDto.getIsComplete())
                .orderBy(true, userActivityDto.getOrderMode(), UserActivity::getUpdateTime)
                .page(page);

        List<UserActivity> userActivities = page.getRecords();
        if (CollectionUtil.isEmpty(userActivities)) {
            return new PageVo<>(null, 0L);
        }
        //封装活动名称
        Set<Long> activityIds = userActivities.stream().map(UserActivity::getActivityId).collect(Collectors.toSet());
        Map<Long, String> activityMap = new LambdaQueryChainWrapper<>(activityMapper)
                .in(Activity::getId, activityIds)
                .select(Activity::getId, Activity::getName)
                .list()
                .stream().collect(Collectors.toMap(Activity::getId, Activity::getName));
        userActivities = userActivities.stream()
                .peek(userActivity -> userActivity.setActivityName(activityMap.get(userActivity.getActivityId())))
                .collect(Collectors.toList());
        return new PageVo<>(userActivities, page.getTotal());
    }

    @Override
    public PageVo<?> listClockInInfo(ActivityClockInInfoPageDto pageDto) {
        Page<UserActivity> page = new Page<>(pageDto.getPageNum(), pageDto.getPageSize());
        lambdaQuery()
                .eq(UserActivity::getActivityId, pageDto.getActivityId())
                .eq(Objects.nonNull(pageDto.getIsComplete()), UserActivity::getIsComplete, pageDto.getIsComplete())
                .orderByDesc(UserActivity::getClockTime)
                .page(page);
        List<UserActivity> userActivityList = page.getRecords();
        if (CollectionUtil.isEmpty(userActivityList)) {
            return new PageVo<>();
        }
        //封装学生信息
        Set<Long> userIds = userActivityList.stream().map(UserActivity::getUserId).collect(Collectors.toSet());
        Map<Long, User> map = new LambdaQueryChainWrapper<>(userMapper)
                .in(User::getId, userIds)
                .select(User::getId, User::getName, User::getFromClass, User::getScore)
                .list()
                .stream().collect(Collectors.toMap(User::getId, user -> user));
        userActivityList = userActivityList.stream().peek(userActivity -> {
            User user = map.get(userActivity.getUserId());
            userActivity.setName(user.getName());
            userActivity.setFromClass(user.getFromClass());
            userActivity.setScore(user.getScore());
        }).collect(Collectors.toList());

        return new PageVo<>(userActivityList, page.getTotal());
    }
}




