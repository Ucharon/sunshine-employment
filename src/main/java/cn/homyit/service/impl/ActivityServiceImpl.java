package cn.homyit.service.impl;

import ch.qos.logback.core.util.TimeUtil;
import cn.homyit.entity.DO.*;
import cn.homyit.entity.DTO.ActivityDto;
import cn.homyit.entity.DTO.ActivityPageDto;
import cn.homyit.entity.DTO.ClockInDto;
import cn.homyit.entity.VO.PageVo;
import cn.homyit.enums.ResultCodeEnum;
import cn.homyit.enums.ScoreRecordTypesEnum;
import cn.homyit.exception.BizException;
import cn.homyit.mapper.ActivityMapper;
import cn.homyit.service.*;
import cn.homyit.utils.*;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.Synchronized;
import org.bouncycastle.util.Integers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static cn.homyit.constant.CommonConstants.ACTIVITY_DISABLE;
import static cn.homyit.constant.CommonConstants.ACTIVITY_ENABLE;
import static cn.homyit.constant.RedisConstants.*;
import static cn.homyit.enums.ResultCodeEnum.*;

/**
 * @author charon
 * @description 针对表【tb_activity(活动表)】的数据库操作Service实现
 * @createDate 2023-03-28 15:23:49
 */
@Service
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity>
        implements ActivityService {

    @Resource
    private UserService userService;
    @Resource
    private ActivityTagService activityTagService;
    @Resource
    private CacheClient cacheClient;
    @Resource
    private DirectService directService;
    @Resource
    private UserActivityService userActivityService;
    @Resource
    private RedisCache redisCache;
    @Resource
    private ScoreRecordsService scoreRecordsService;
    @Resource
    private BitMapClient bitMapClient;

    @Override
    public PageVo<?> activityList(ActivityPageDto activityPageDto) {
        Page<Activity> page = new Page<>(activityPageDto.getPageNum(), activityPageDto.getPageSize());

        LambdaQueryWrapper<Activity> queryWrapper = new QueryWrapper<Activity>()
                //指定排序字段与排序规则
                .orderBy(true, activityPageDto.getOrderMode()
                        , activityPageDto.getOrderField().getFieldName())
                .lambda()
                //活动是否下架
                .eq(Activity::getStatus, ACTIVITY_ENABLE)
                //老师id检索
                .eq(Objects.nonNull(activityPageDto.getUserId()), Activity::getUserId, activityPageDto.getUserId())
                //标签检索
                .eq(Objects.nonNull(activityPageDto.getTagId()), Activity::getTagId, activityPageDto.getTagId())
                //方向检索
                .eq(Objects.nonNull(activityPageDto.getDirectId()),
                        Activity::getDirectId, activityPageDto.getDirectId())
                //活动名称模糊检索
                .like(Objects.nonNull(activityPageDto.getName()),
                        Activity::getName, activityPageDto.getName())
                //地点名称模糊检索
                .like(Objects.nonNull(activityPageDto.getLocationName()),
                        Activity::getLocation, activityPageDto.getLocationName())
                //指定查询的列名
                .select(Activity::getId, Activity::getName, Activity::getUserId, Activity::getTagId
                        , Activity::getThumbnail, Activity::getScore, Activity::getLocation, Activity::getSignNums
                        , Activity::getSignEnd, Activity::getActivityBegin, Activity::getActivityEnd
                        , Activity::getCreateTime, Activity::getDirectId);

        //筛选是否已经结束的活动
        Integer isEnd = activityPageDto.getIsEnd();
        if (Objects.nonNull(isEnd)) {
            queryWrapper
                    .le(isEnd.equals(1), Activity::getActivityEnd, TimeUtils.getCurrentTime())
                    .gt(isEnd.equals(0), Activity::getActivityEnd, TimeUtils.getCurrentTime());
        }

        page(page, queryWrapper);
        List<Activity> activityList = page.getRecords();
        if (activityList.isEmpty()) {
            throw new BizException(ACTIVITY_NOT_EXIST);
        }
        //封装活动是否已结束
        activityList.forEach(activity -> activity
                .setIsEnd(activity.getActivityEnd().compareTo(TimeUtils.getCurrentTime()) <= 0));

        //获取到老师的姓名
        Set<Long> userIds = activityList.stream()
                .map(Activity::getUserId).collect(Collectors.toSet());
        Map<Long, String> userMap = userService.lambdaQuery()
                .in(User::getId, userIds)
                .select(User::getId, User::getName)
                .list()
                .stream().collect(Collectors.toMap(User::getId, User::getName));
        //将姓名封装到结果
        activityList = activityList.stream()
                .peek(activity -> activity.setUserName(userMap.get(activity.getUserId())))
                .collect(Collectors.toList());

        //获取标签id
        Map<Integer, String> tagMap = activityTagService.list()
                .stream().collect(Collectors.toMap(ActivityTag::getId, ActivityTag::getName));
        //将标签名封装到结果
        activityList.forEach(activity -> activity.setTagName(tagMap.get(activity.getTagId())));

        //获取方向id
        Map<Integer, String> directMap = directService.list()
                .stream().collect(Collectors.toMap(Direct::getId, Direct::getName));
        //将方向名称封装到结果
        activityList.forEach(activity -> activity.setDirectName(directMap.get(activity.getDirectId())));

        //统计活动的完成人数
        activityList.forEach(activity -> {
                    long count = userActivityService.count(userActivityService.lambdaUpdate()
                            .eq(UserActivity::getActivityId, activity.getId())
                            .eq(UserActivity::getIsComplete, 1).getWrapper());
                    activity.setCompleteNum(Math.toIntExact(count));
                }
        );

        //封装成pageVo并返回
        return new PageVo<>(activityList, page.getTotal());
    }

    @Override
    public Activity getActivityById(Long id) {
        return cacheClient.queryWithMutex(ACTIVITY_CACHE_KEY, ACTIVITY_LOCK_KEY,
                id, Activity.class, this::getActivityByMapper,
                ACTIVITY_CACHE_TTL, TimeUnit.HOURS);
    }


    public Activity getActivityByMapper(Long id) {
        Activity activity = lambdaQuery()
                //检查活动是否下架
                .eq(Activity::getStatus, ACTIVITY_ENABLE)
                .eq(Activity::getId, id)
                .one();
        activity.setTagName(activityTagService.getById(activity.getTagId()).getName());
        activity.setUserName(userService.getById(activity.getUserId()).getName());
        activity.setDirectName(directService.getById(activity.getDirectId()).getName());
        return activity;
    }

    @Override
    @Transactional
    public void signActivity(Long activityId) {
        //首先检测该活动是否已经下架
        Optional.ofNullable(lambdaQuery()
                .eq(Activity::getStatus, ACTIVITY_ENABLE)
                .eq(Activity::getId, activityId)
                .ge(Activity::getSignEnd, TimeUtils.getCurrentTime())
                .one()).orElseThrow(() -> new BizException(ResultCodeEnum.ORDER_NOT_EXIST));
        //用户不可以多次报名活动，防止多线程下存在问题，这里用个互斥锁
        //这里的互斥锁简单粗暴-为全局锁，同一时间永远只能有一个线程获取到锁，
        //会导致性能问题，应该根据用户id加锁
        //signActivityByOverallLock(activityId);
        //下面是用用户id作为锁的标示，用redis实现简单的互斥锁（nx字段）
        signActivityByUserLock(activityId);
    }

    private void signActivityByUserLock(Long activityId) {
        Long userId = UserUtils.getUser().getUser().getId();
        String lockKey = LOCK_USER_KEY + userId;
        //获取互斥锁
        try {
            boolean success = cacheClient.tryLock(lockKey);
            //是否获取成功
            if (!success) {
                //获取失败，休眠并重试
                Thread.sleep(50);
                signActivityByUserLock(activityId);
            }
            //成功获取锁
            //首先判断用户是否以及报名该活动
            if (Optional.ofNullable(userActivityService.lambdaQuery()
                    .eq(UserActivity::getUserId, userId)
                    .eq(UserActivity::getActivityId, activityId)
                    .one()).isPresent()) {
                //存在
                throw new BizException(ResultCodeEnum.DUPLICATE_REGISTRATION);
            }

            //执行报名操作
            UserActivity userActivity = new UserActivity();
            userActivity.setUserId(userId);
            userActivity.setActivityId(activityId);
            userActivityService.save(userActivity);

            //对活动报名人数进行更新
            lambdaUpdate()
                    .eq(Activity::getId, activityId)
                    .setSql("sign_nums = sign_nums + 1")
                    .update();
            //删除缓存
            redisCache.deleteObject(ACTIVITY_CACHE_KEY + activityId);
        } catch (InterruptedException e) {
            throw new RuntimeException();
        } finally {
            //释放锁
            cacheClient.unlock(lockKey);
        }
    }

    @Synchronized
    private void signActivityByOverallLock(Long activityId) {
        //首先判断用户是否以及报名该活动
        if (Optional.ofNullable(userActivityService.lambdaQuery()
                .eq(UserActivity::getActivityId, activityId)
                .one()).isPresent()) {
            //存在
            throw new BizException(ResultCodeEnum.DUPLICATE_REGISTRATION);
        }
        //执行报名操作
        UserActivity userActivity = new UserActivity();
        userActivity.setUserId(UserUtils.getUser().getUser().getId());
        userActivity.setActivityId(activityId);
        userActivityService.save(userActivity);
    }

    @Override
    public void updateActivity(ActivityDto activityDto, Long id) {
        Activity activity = BeanCopyUtils.copyBean(activityDto, Activity.class);
        activity.setId(id);
        activity.setActivityEnd(activityDto.getActivityBegin().plusHours(activityDto.getStanding()));
        updateById(activity);
        //删除缓存
        redisCache.deleteObject(ACTIVITY_CACHE_KEY + id.toString());
    }

    @Override
    public void saveActivity(ActivityDto activityDto) {
        Activity activity = BeanCopyUtils.copyBean(activityDto, Activity.class);
        activity.setActivityEnd(activityDto.getActivityBegin().plusHours(activityDto.getStanding()));
        if (StrUtil.isBlank(activityDto.getThumbnail())) {
            activity.setThumbnail("https://typora-1312272916.cos.ap-shanghai.myqcloud.com//img0a7fcf6c1ee63dd17fce8c0e9a351d82_750.jpg");
        }
        save(activity);
        //删除空缓存
        redisCache.deleteObject(ACTIVITY_CACHE_KEY + activity.getId().toString());
    }

    @Override
    public void deleteActivity(List<Long> ids) {
        lambdaUpdate().in(!ids.isEmpty(), Activity::getId, ids)
                .set(Activity::getStatus, ACTIVITY_DISABLE)
                .update();
        //删除缓存
        redisCache.deleteObject(ACTIVITY_CACHE_KEY + ids);
    }

    @Override
    public void cancelSign(Long id) {
        //查询该活动是否已经结束或不存在
        Optional.ofNullable(userActivityService.lambdaQuery()
                .eq(UserActivity::getId, id)
                .eq(UserActivity::getIsComplete, 0)
                .one()).orElseThrow(() -> new BizException(ResultCodeEnum.ACTIVITY_IS_END));
        //否则删除活动记录
        removeById(id);
    }

    @Override
    public void clockInActivity(ClockInDto clockInDto) {
        Long userId = UserUtils.getUser().getUser().getId();
        //查询活动是否已经开始
        //TODO 多线程不安全，可以尝试加锁
        Optional.ofNullable(lambdaQuery()
                        .eq(Activity::getStatus, ACTIVITY_ENABLE)
                        .eq(Activity::getId, clockInDto.getActivityId())
                        .le(Activity::getActivityBegin, TimeUtils.getCurrentTime())
                        .ge(Activity::getActivityEnd, TimeUtils.getCurrentTime())
                        .one())
                .orElseThrow(() -> new BizException(ACTIVITY_OUT_OF_RANGE));
        //查询报名详情并修改
        //查询报名信息
        UserActivity userActivity = Optional.ofNullable(userActivityService.lambdaQuery()
                .eq(UserActivity::getUserId, userId)
                .eq(UserActivity::getActivityId, clockInDto.getActivityId())
                .eq(UserActivity::getIsComplete, 0).one()).orElseThrow(() -> new BizException(NOT_SIGN));
        userActivity.setIsComplete(1);
        userActivity.setClockImg(clockInDto.getImgUrl());
        userActivity.setClockTime(TimeUtils.getCurrentTime());
        userActivityService.updateById(userActivity);

        //打卡完成，对积分操作
        Activity activity = getActivityById(clockInDto.getActivityId());
        userService.lambdaUpdate()
                .setSql("score = score + " + activity.getScore())
                .update();
        //删除用户缓存
        redisCache.deleteObject(USER_CACHE_KEY + userId);
        //记录积分流水
        scoreRecordsService.insertRecord(UserUtils.getUser().getUser().getScore(),
                activity.getScore(), userActivity.getId(), ScoreRecordTypesEnum.ACTIVITY_IN);

        //记录用户该日打卡情况
        bitMapClient.recordsClockIn(ACTIVITY_CLOCK_KEY);
    }

    @Override
    public List<Long> getActivityIdsByName(String activityName) {
        return lambdaQuery()
                .like(Activity::getName, activityName)
                .select(Activity::getId)
                .list()
                .stream().map(Activity::getId).collect(Collectors.toList());
    }


}




