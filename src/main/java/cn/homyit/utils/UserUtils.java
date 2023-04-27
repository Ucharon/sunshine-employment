package cn.homyit.utils;

import cn.homyit.entity.DO.LoginUser;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @program: graduate-website
 * @description: 获取登录用户信息工具类
 * @author: Charon
 * @create: 2023-03-26 16:50
 **/
public class UserUtils {

    /**
     * 获取当前用户信息
     * @return
     */
    public static LoginUser getUser() {
        return ((LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }

}
