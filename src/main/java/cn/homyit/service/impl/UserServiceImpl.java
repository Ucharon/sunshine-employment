package cn.homyit.service.impl;

import cn.homyit.constant.CommonConstants;
import cn.homyit.constant.RedisConstants;
import cn.homyit.entity.DO.*;
import cn.homyit.entity.DTO.LoginUserDto;
import cn.homyit.entity.DTO.UserInfoDto;
import cn.homyit.entity.DTO.UserPageDto;
import cn.homyit.entity.DTO.UserPasswordDto;
import cn.homyit.entity.VO.PageVo;
import cn.homyit.enums.ResultCodeEnum;
import cn.homyit.exception.BizException;
import cn.homyit.mapper.UserMapper;
import cn.homyit.service.*;
import cn.homyit.utils.*;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static cn.homyit.constant.CommonConstants.RESUME_UPLOADING_SCORE;
import static cn.homyit.constant.CommonConstants.USER_DISABLE;
import static cn.homyit.constant.RedisConstants.*;
import static cn.homyit.enums.ScoreRecordTypesEnum.DAILY_CLOCK_IN;

/**
 * @author charon
 * @description 针对表【sys_user(用户表)】的数据库操作Service实现
 * @createDate 2023-03-25 17:21:07
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private DailyClockInService dailyClockInService;
    @Resource
    private RedisCache redisCache;
    @Resource
    private CacheClient cacheClient;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private BitMapClient bitMapClient;
    @Resource
    private RoleService roleService;
    @Resource
    private DirectService directService;
    @Resource
    private ScoreRecordsService scoreRecordsService;

    @Override
    public Map<String, String> login(LoginUserDto userDto) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDto.getUserName(), userDto.getPassword());
        Optional<Authentication> authenticationOptional =
                Optional.ofNullable(authenticationManager.authenticate(authenticationToken));

        //走到这里，说明账号密码正确！
        // (开始将用户数据存入redis缓存)---不必要！！
        //获取到用户信息
        LoginUser loginUser = (LoginUser) authenticationOptional
                .orElseThrow(() -> new BizException(ResultCodeEnum.LOGIN_ERROR))
                .getPrincipal();

        //生成对应的token
        String token = UUID.randomUUID().toString(true);
        Map<String, String> map = new HashMap<>();
        map.put(CommonConstants.TOKEN, token);
        //将token：用户ID组成的键值对存入redis
        redisCache.setCacheObject(LOGIN_TOKEN_KEY + token,
                loginUser.getUser().getId(), RedisConstants.LOGIN_USER_TTL, TimeUnit.MINUTES);

        //完善用户的其他信息：角色名称、方向信息
        //查询出角色名称，加入到user对象中
        cacheClient.saveLoginStatus(token, loginUser.getUser().getId());

        return map;
    }


    @Override
    public UserInfoDto getPersonalInfo() {
        //获取user对象并将其转换为userInfoDto
        User user = UserUtils.getUser().getUser();

        return BeanCopyUtils.copyBean(user, UserInfoDto.class);
    }

    @Override
    @Transactional
    public void updateUserInfo(UserInfoDto userInfoDto) {
        Long userId = UserUtils.getUser().getUser().getId();
        User user = BeanCopyUtils.copyBean(userInfoDto, User.class);
        user.setId(userId);
        //如果用户第一次添加简历
        if (StrUtil.isBlank(user.getResumeFile())) {
            user.setScore(UserUtils.getUser().getUser().getScore() + RESUME_UPLOADING_SCORE);
        }
        //更新基础信息-邮箱-电话号码-头像
        updateById(user);
        //删除缓存
        redisCache.deleteObject(USER_CACHE_KEY + userId);
    }

    @Override
    public void logout(Long id) {
        //用户登出操作，这里将用户所有的token全部失效
        id = Optional.ofNullable(id).orElse(UserUtils.getUser().getUser().getId());
        String loginStatusKey = LOGIN_STATUS_KEY + id;
        Set<String> tokenSet = Objects
                .requireNonNull(stringRedisTemplate.opsForSet().members(loginStatusKey))
                .stream()
                .map(o -> LOGIN_TOKEN_KEY + o)
                .collect(Collectors.toSet());
        redisCache.deleteObject(tokenSet);
        redisCache.deleteObject(loginStatusKey);
    }

    @Override
    public String getQrUUID() {
        //获取一个随机的key
        String uuid = UUID.randomUUID().toString(false);
        String key = QRCODE_KEY + uuid;
        //将用户id设置为其值
        Long id = UserUtils.getUser().getUser().getId();
        //加入到redis
        redisCache.setCacheObject(key, id, QRCODE_TTL, TimeUnit.MINUTES);
        //并将UUID返回
        return uuid;
    }

    @Override
    public Long userUuidGetUserId(String uuid) {
        return (Long) Optional.ofNullable(redisCache.getCacheObject(QRCODE_KEY + uuid))
                .orElseThrow(() -> new BizException(ResultCodeEnum.QR_FAILURE));
    }

    @Override
    public void updateUserPassword(UserPasswordDto userPasswordDto) {
        LoginUser user = UserUtils.getUser();
        //首先校对原密码
        boolean matches = passwordEncoder.matches(userPasswordDto.getOriginalPassword(),
                user.getPassword());
        //如果原密码不正确
        if (!matches) {
            throw new BizException(ResultCodeEnum.PASSWORD_ERROR);
        }
        //开始更新密码
        lambdaUpdate()
                .eq(User::getId, user.getUser().getId())
                .set(User::getPassword, passwordEncoder.encode(userPasswordDto.getNewPassword()))
                .update();
        //删除缓存
        redisCache.deleteObject(USER_CACHE_KEY + user.getUser().getId());
    }

    @Override
    public void disableUsers(List<Long> ids) {
        lambdaUpdate()
                .in(User::getId, ids)
                .set(User::getStatus, USER_DISABLE)
                .update();
    }

    @Override
    public void saveDailyClockIn(String url) {
        // TODO 多线程不安全
        Long userId = UserUtils.getUser().getUser().getId();
        Boolean success = bitMapClient.recordsClockIn(DAILY_CLOCK_KEY);
        //如果设置值前为true的话，则表示已打卡
        if (success) {
            throw new BizException(ResultCodeEnum.DUPLICATE_CLOCK_IN);
        }
        //开始将打卡信息存入数据库
        DailyClockIn dailyClockIn = new DailyClockIn();
        dailyClockIn.setUserId(userId);
        dailyClockIn.setImg(url);
        dailyClockInService.save(dailyClockIn);
        //增加积分
        lambdaUpdate()
                .eq(User::getId, userId)
                .setSql("score = score + 1")
                .update();

        //删除缓存
        redisCache.deleteObject(USER_CACHE_KEY + userId);
        scoreRecordsService.insertRecord(UserUtils.getUser().getUser().getScore(), 1L, null, DAILY_CLOCK_IN);
    }

    @Override
    public PageVo<?> userList(UserPageDto userPageDto) {
        Page<User> page = new Page<>(userPageDto.getPageNum(), userPageDto.getPageSize());
        lambdaQuery()
                .like(Objects.nonNull(userPageDto.getUserName()), User::getUserName, userPageDto.getUserName())
                .like(Objects.nonNull(userPageDto.getName()), User::getName, userPageDto.getName())
                .eq(Objects.nonNull(userPageDto.getRoleId()), User::getRoleId, userPageDto.getRoleId())
                .like(Objects.nonNull(userPageDto.getFromClass()), User::getFromClass, userPageDto.getFromClass())
                .eq(Objects.nonNull(userPageDto.getStatus()), User::getStatus, userPageDto.getStatus())
                .eq(Objects.nonNull(userPageDto.getFirstDirect()), User::getFirstDirect, userPageDto.getFirstDirect())
                .eq(Objects.nonNull(userPageDto.getSecondDirect()), User::getSecondDirect, userPageDto.getSecondDirect())
                .orderByAsc(User::getUserName)
                .page(page);
        List<User> userList = page.getRecords();
        if (CollectionUtil.isEmpty(userList)) {
            return new PageVo<>();
        }

        //封装额外信息
        Map<Integer, String> roleMap = roleService.list()
                .stream().collect(Collectors.toMap(Role::getId, Role::getName));
        Map<Integer, String> directMap = directService.list()
                .stream().collect(Collectors.toMap(Direct::getId, Direct::getName));
        userList.forEach(user -> {
            //角色
            user.setRoleName(roleMap.get(user.getRoleId()));
            //第一方向
            user.setFirstDirectName(directMap.get(user.getFirstDirect()));
            //第二方向
            user.setSecondDirectName(directMap.get(user.getSecondDirect()));
        });

        return new PageVo<>(userList, page.getTotal());
    }


}




