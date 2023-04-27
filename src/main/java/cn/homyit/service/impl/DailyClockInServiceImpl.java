package cn.homyit.service.impl;

import cn.homyit.entity.DO.User;
import cn.homyit.entity.DTO.DailyClockInPageDto;
import cn.homyit.entity.VO.PageVo;
import cn.homyit.mapper.UserMapper;
import cn.homyit.service.UserService;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.homyit.entity.DO.DailyClockIn;
import cn.homyit.service.DailyClockInService;
import cn.homyit.mapper.DailyClockInMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author charon
 * @description 针对表【tb_daily_clock_in】的数据库操作Service实现
 * @createDate 2023-04-04 18:35:54
 */
@Service
public class DailyClockInServiceImpl extends ServiceImpl<DailyClockInMapper, DailyClockIn>
        implements DailyClockInService {

    @Resource
    private UserMapper userMapper;

    @Override
    public PageVo<?> listDailyClockIn(DailyClockInPageDto pageDto) {
        Page<DailyClockIn> page = new Page<>(pageDto.getPageNum(), pageDto.getPageSize());
        lambdaQuery()
                //根据用户id检索
                .eq(Objects.nonNull(pageDto.getUserId()), DailyClockIn::getUserId, pageDto.getUserId())
                //时间范围
                .ge(Objects.nonNull(pageDto.getBeginTime()), DailyClockIn::getCreateTime, pageDto.getBeginTime())
                .le(Objects.nonNull(pageDto.getEndTime()), DailyClockIn::getCreateTime, pageDto.getEndTime())
                .orderByDesc(DailyClockIn::getCreateTime)
                .page(page);
        List<DailyClockIn> dailyClockInList = page.getRecords();
        if (CollectionUtil.isEmpty(dailyClockInList)) {
            return new PageVo<>();
        }
        //封装额外信息
        //如学生信息
        Set<Long> userIds = dailyClockInList.stream().map(DailyClockIn::getUserId).collect(Collectors.toSet());
        Map<Long, User> stuMap = new LambdaQueryChainWrapper<>(userMapper)
                .in(User::getId, userIds)
                .select(User::getId, User::getName, User::getFromClass, User::getScore)
                .list()
                .stream().collect(Collectors.toMap(User::getId, user -> user));
        dailyClockInList.forEach(dailyClockIn -> {
            User user = stuMap.get(dailyClockIn.getUserId());
            dailyClockIn.setStuName(user.getName());
            dailyClockIn.setScore(user.getScore());
            dailyClockIn.setFromClass(user.getFromClass());
        });

        return new PageVo<>(dailyClockInList, page.getTotal());
    }
}




