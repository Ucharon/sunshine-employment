package cn.homyit.service.impl;

import cn.homyit.entity.DO.LoginUser;
import cn.homyit.entity.DO.User;
import cn.homyit.enums.ResultCodeEnum;
import cn.homyit.exception.BizException;
import cn.homyit.mapper.RoleMapper;
import cn.homyit.mapper.UserMapper;
import cn.homyit.service.RoleService;
import cn.homyit.service.UserService;
import cn.homyit.utils.CacheClient;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.mapper.Mapper;
import com.baomidou.mybatisplus.extension.conditions.ChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

import static cn.homyit.constant.CommonConstants.USER_ENABLE;
import static cn.homyit.enums.ResultCodeEnum.LOGIN_ERROR;

/**
 * @program: graduate-website
 * @description:
 * @author: Charon
 * @create: 2023-03-25 16:25
 **/
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private RoleMapper roleMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查询用户信息
        //顺便查询该学生账号是否已经停用
        User user = Optional.ofNullable(new LambdaQueryChainWrapper<>(userMapper)
                .eq(User::getUserName, username)
                .eq(User::getStatus, USER_ENABLE)
                .one()).orElseThrow(() -> new BizException(LOGIN_ERROR));

        //根据用户查询权限信息 添加到LoginUser中
        //根据角色id查询到所有的权限名
        List<String> permissions = roleMapper.selectPerms(user.getRoleId());

        return new LoginUser(user, permissions);
    }
}
