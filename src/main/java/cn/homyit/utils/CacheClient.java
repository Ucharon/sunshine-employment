package cn.homyit.utils;

import cn.homyit.constant.RedisConstants;
import cn.homyit.entity.DO.Direct;
import cn.homyit.entity.DO.LoginUser;
import cn.homyit.entity.DO.User;
import cn.homyit.mapper.DirectMapper;
import cn.homyit.mapper.RoleMapper;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import static cn.homyit.constant.RedisConstants.*;

/**
 * @program: graduate-website
 * @description: 缓存工具类
 * @author: Charon
 * @create: 2023-03-27 13:42
 **/

@Slf4j
@Component
public class CacheClient {

    @Resource
    private RoleMapper roleMapper;
    @Resource
    private DirectMapper directMapper;
    @Resource
    private RedisCache redisCache;

    private final StringRedisTemplate stringRedisTemplate;
    private static final ExecutorService CACHE_REBUILD_EXECUTOR = Executors.newFixedThreadPool(10);

    public CacheClient(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public <T> void set(String key, T value, Long time, TimeUnit unit) {
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(value), time, unit);
    }

    /**
     * 获取锁
     */
    public boolean tryLock(String key) {
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(key, "1", LOCK_USER_TTL, TimeUnit.SECONDS);
        return BooleanUtil.isTrue(flag);
    }

    /**
     * 释放锁
     */
    public void unlock(String key) {
        stringRedisTemplate.delete(key);
    }

    public <T> void setWithLogicalExpire(String key, T value, Long time, TimeUnit unit) {
        //设置逻辑过期
        RedisData<T> redisData = new RedisData<>();
        redisData.setData(value);
        redisData.setExpireTime(LocalDateTime.now().plusSeconds(unit.toSeconds(time)));
        //写入redis
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(redisData));
    }

    /**
     * redis实现缓存-利用互斥锁解决缓存击穿
     *
     * @param keyPrefix
     * @param id
     * @param type
     * @param dbFallback
     * @param time
     * @param unit
     * @param <T>
     * @param <ID>
     * @return
     */
    public <T, ID> T queryWithMutex(String keyPrefix, String lockKeyPrefix, ID id, Class<T> type, Function<ID, T> dbFallback, Long time, TimeUnit unit) {
        //1.从redis查询对应对象缓存
        String key = keyPrefix + id;
        String json = stringRedisTemplate.opsForValue().get(key);

        //2.判断是否存在
        if (StrUtil.isNotBlank(json)) {
            //3.存在，直接返回
            return JSONUtil.toBean(json, type);
        }
        //判断命中是否是空值
        if (json != null) {
            //返回错误信息
            return null;
        }

        //4.实现缓存重建
        //4.1 获取互斥锁
        String lockKey = lockKeyPrefix + id;
        T t = null;
        try {
            boolean isLock = tryLock(lockKey);
            //4.2 是否获取成功
            if (!isLock) {
                //4.3 失败，休眠并重试
                Thread.sleep(50);
                return queryWithMutex(keyPrefix, lockKeyPrefix, id, type, dbFallback, time, unit);
            }

            //4.5 成功，根据id查询数据库
            t = dbFallback.apply(id);
            //缓存穿透的情况-这里缓存空值
            if (t == null) {
                //将空值写入redis
                stringRedisTemplate.opsForValue().set(key, "", CACHE_NULL_TTL, TimeUnit.MINUTES);
                //5.不存在，返回错误
                return null;
            }

            //6.存在，写入redis
            this.set(key, t, time, unit);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            //7.释放互斥锁
            unlock(lockKey);
        }
        //8.返回
        return t;
    }

    /**
     * 将用户登录状态加入到redis集合中
     *
     * @param token
     */
    public void saveLoginStatus(String token, Long userId) {
        stringRedisTemplate.opsForSet().add(LOGIN_STATUS_KEY + userId, token);
    }

    /**
     * 完善用户的其他信息：角色名称、方向信息
     * 并查询出角色名称，加入到user对象中
     *
     * @param loginUser
     */
    public void saveUserToRedis(LoginUser loginUser) {
        User user = loginUser.getUser();
        String roleName = roleMapper.selectById(user.getRoleId()).getName();
        user.setRoleName(roleName);
        //再查询出第一方向与第二方向名称，加入到对象中
        Map<Integer, String> map = new LambdaQueryChainWrapper<>(directMapper)
                .in(Objects.nonNull(user.getFirstDirect()), Direct::getId, user.getFirstDirect())
                .or()
                .in(Objects.nonNull(user.getSecondDirect()), Direct::getId, user.getSecondDirect())
                .list()
                .stream().collect(Collectors.toMap(Direct::getId, Direct::getName));
        user.setFirstDirectName(map.get(user.getFirstDirect()));
        user.setSecondDirectName(map.get(user.getSecondDirect()));
        //将loginUser对象存入redis，这里是userId：loginUser形式存入redis
        redisCache.setCacheObject(USER_CACHE_KEY + user.getId(), loginUser,
                RedisConstants.USER_CACHE_TTL, TimeUnit.HOURS);
    }


}
