package cn.homyit.service.impl;

import cn.homyit.entity.DO.Appoint;
import cn.homyit.entity.DO.User;
import cn.homyit.entity.DTO.AppointPageDto;
import cn.homyit.entity.VO.PageVo;
import cn.homyit.enums.AppointStatusEnum;
import cn.homyit.enums.AppointTypeEnum;
import cn.homyit.enums.ResultCodeEnum;
import cn.homyit.exception.BizException;
import cn.homyit.mapper.AppointMapper;
import cn.homyit.mapper.UserMapper;
import cn.homyit.service.AppointService;
import cn.homyit.utils.UserUtils;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author charon
 * @description 针对表【tb_appoint(预约表)】的数据库操作Service实现
 * @createDate 2023-04-05 23:36:38
 */
@Service
public class AppointServiceImpl extends ServiceImpl<AppointMapper, Appoint>
        implements AppointService {

    @Resource
    private UserMapper userMapper;

    @Override
    public void saveAppoint(Appoint appoint) {
        User user = UserUtils.getUser().getUser();
        //1. 设置预约状态
        appoint.setAppointStatusEnum(AppointStatusEnum.UNPROCESSED);
        //2. 填写学生信息
        appoint.setStuId(user.getId());

        //2.1 如果为简历制作类型
        if (appoint.getAppointTypeEnum().equals(AppointTypeEnum.RESUME_MAKING)) {
            //自动将用户的简历字段填入预约中
            String file = Optional.ofNullable(user.getResumeFile())
                    .orElseThrow(() -> new BizException(ResultCodeEnum.RESUME_NOT_UPLOADED));
            appoint.setResumeFile(file);
        }

        //3. 插入到数据库
        save(appoint);

        //TODO 及时发送信息给对应的老师（短信or邮箱）
    }

    @Override
    public PageVo<?> appointList(AppointPageDto appointPageDto) {
        Page<Appoint> page = new Page<>(appointPageDto.getPageNum(), appointPageDto.getPageSize());
        lambdaQuery()
                .eq(Objects.nonNull(appointPageDto.getStuId()),
                        Appoint::getStuId, appointPageDto.getStuId())
                .eq(Objects.nonNull(appointPageDto.getTeaId()),
                        Appoint::getTeaId, appointPageDto.getTeaId())
                .eq(Objects.nonNull(appointPageDto.getAppointTypeEnum()),
                        Appoint::getAppointTypeEnum, appointPageDto.getAppointTypeEnum())
                .eq(Objects.nonNull(appointPageDto.getAppointStatusEnum()),
                        Appoint::getAppointStatusEnum, appointPageDto.getAppointStatusEnum())
                //指定排序方式
                .orderByDesc(Appoint::getUpdateTime)
                .page(page);
        List<Appoint> appointList = page.getRecords();
        if (CollectionUtil.isEmpty(appointList)) {
            return new PageVo<>();
        }
        //查询出学生的班级和姓名
        Set<Long> stuIds = appointList.stream().map(Appoint::getStuId).collect(Collectors.toSet());
        Map<Long, User> stuMap = new LambdaQueryChainWrapper<>(userMapper)
                .in(User::getId, stuIds)
                .select(User::getId, User::getName, User::getFromClass)
                .list()
                .stream().collect(Collectors.toMap(User::getId, user -> user));
        appointList = appointList.stream().peek(appoint -> {
            User user = stuMap.get(appoint.getStuId());
            appoint.setStuName(user.getName());
            appoint.setFromClass(user.getFromClass());
        }).collect(Collectors.toList());

        //查询出老师的姓名和电话
        Set<Long> teaIds = appointList.stream().map(Appoint::getTeaId).collect(Collectors.toSet());
        Map<Long, User> TeaMap = new LambdaQueryChainWrapper<>(userMapper)
                .in(User::getId, teaIds)
                .select(User::getId, User::getName, User::getPhonenumber)
                .list()
                .stream().collect(Collectors.toMap(User::getId, user -> user));
        appointList = appointList.stream().peek(appoint -> {
            User user = TeaMap.get(appoint.getTeaId());
            appoint.setTeaName(user.getName());
            appoint.setTeaPhonenumber(user.getPhonenumber());
        }).collect(Collectors.toList());

        return new PageVo<>(appointList, page.getTotal());
    }

    @Override
    public void teacherUpdateAppoint(Appoint appoint) {
        if (getById(appoint.getId()).getAppointStatusEnum().equals(appoint.getAppointStatusEnum())) {
            throw new BizException(ResultCodeEnum.DUPLICATE_SUBMISSION);
        }
        updateById(appoint);
        // TODO 微信小程序发送模板消息给学生
    }

}




