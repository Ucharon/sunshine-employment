package cn.homyit.filter;

import cn.homyit.constant.CommonConstants;
import cn.homyit.constant.RedisConstants;
import cn.homyit.entity.DO.LoginUser;
import cn.homyit.entity.DO.Order;
import cn.homyit.entity.DO.User;
import cn.homyit.entity.DO.UserActivity;
import cn.homyit.enums.ResultCodeEnum;
import cn.homyit.exception.BizException;
import cn.homyit.mapper.OrderMapper;
import cn.homyit.mapper.RoleMapper;
import cn.homyit.mapper.UserActivityMapper;
import cn.homyit.mapper.UserMapper;
import cn.homyit.service.UserService;
import cn.homyit.utils.CacheClient;
import cn.homyit.utils.RedisCache;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @program: graduate-website
 * @description: 认证过滤器
 * @author: Charon
 * @create: 2023-03-25 21:45
 **/
@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Resource
    private RedisCache redisCache;
    @Resource
    private UserMapper userMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private CacheClient cacheClient;
    @Resource
    private UserActivityMapper userActivityMapper;
    @Resource
    private OrderMapper orderMapper;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        //获取token
        String token = request.getHeader(CommonConstants.TOKEN);
        if (!StringUtils.hasText(token)) {
            //放行
            filterChain.doFilter(request, response);
            return;
        }

        //从redis中获取用户信息
        //首先获取id
        String tokenKey = RedisConstants.LOGIN_TOKEN_KEY + token;
        Optional<Object> optional = Optional.ofNullable(redisCache.getCacheObject(tokenKey));
        if (!optional.isPresent()) {
            throw new BizException(ResultCodeEnum.LOGIN_INFORMATION_ILLEGAL);
        }
        //刷新token有效期
        redisCache.expire(tokenKey, RedisConstants.LOGIN_USER_TTL, TimeUnit.MINUTES);

        Long userId = (Long) optional.get();
        //获取用户详细信息
        String userCacheKey = RedisConstants.USER_CACHE_KEY + userId;

        LoginUser loginUser = (LoginUser) Optional.ofNullable(redisCache.getCacheObject(userCacheKey)).orElseGet(() -> {
            //未命中缓存，说明用户可能修改了个人信息，开始缓存重建
            //查询数据库
            User user = userMapper.selectById(userId);
            //查询权限信息
            List<String> permissions = roleMapper.selectPerms(user.getRoleId());
            //查询参加个人活动数和商品购买数
            user.setNumOfAct(
                    new LambdaQueryChainWrapper<>(userActivityMapper)
                            .eq(UserActivity::getUserId, user.getId())
                            .count());
            user.setNumOfBuy(
                    new LambdaQueryChainWrapper<>(orderMapper)
                            .eq(Order::getUserId, user.getId())
                            .count());

            LoginUser user1 = new LoginUser(user, permissions);
            //将loginUser存入redis
            cacheClient.saveUserToRedis(user1);
            return user1;
        });

        //存入SecurityContextHolder
        //获取权限信息，将其封装到Authentication中
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }
}
