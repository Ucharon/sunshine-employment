package cn.homyit.service.impl;

import cn.homyit.entity.DO.User;
import cn.homyit.entity.DTO.ExcelUserDto;
import cn.homyit.enums.ResultCodeEnum;
import cn.homyit.exception.BizException;
import cn.homyit.service.ExcelService;
import cn.homyit.service.UserService;
import cn.homyit.utils.BeanCopyUtils;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @program: graduate-website
 * @description:
 * @author: Charon
 * @create: 2023-04-02 17:47
 **/
@Service
public class ExcelServiceImpl implements ExcelService {

    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private UserService userService;

    @Override
    @Transactional
    public void uploadUsers(MultipartFile file) {
        Set<String> userNameSet = userService.lambdaQuery()
                .select(User::getUserName).list()
                .stream().map(User::getUserName).collect(Collectors.toSet());
        //lambda表达式写法，更简洁，适用于业务简单的需求
        try {
            EasyExcel.read(file.getInputStream(),
                    ExcelUserDto.class, new PageReadListener<ExcelUserDto>(excelUserDtos -> {
                        List<User> users = excelUserDtos.stream()
                                //过滤重复学号
                                .filter(excelUserDto -> !userNameSet.contains(excelUserDto.getUserName()))
                                .map(excelUserDto -> {
                                    //对象拷贝
                                    User user = BeanCopyUtils.copyBean(excelUserDto, User.class);
                                    //设置密码
                                    user.setPassword(passwordEncoder.encode(excelUserDto.getPhonenumber()));
                                    //设置头像
                                    user.setAvatar("https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png");
                                    return user;
                                }).collect(Collectors.toList());
                        userService.saveBatch(users);
                    })).sheet().doRead();
        } catch (Exception e) {
            throw new BizException(ResultCodeEnum.EXCEL_FORMAT_ILLEGAL, e);
        }
    }
}
