package cn.homyit.controller;

import cn.homyit.entity.DO.Role;
import cn.homyit.entity.DO.User;
import cn.homyit.entity.DO.UserActivity;
import cn.homyit.entity.DTO.*;
import cn.homyit.entity.VO.PageVo;
import cn.homyit.entity.VO.Result;
import cn.homyit.enums.ResultCodeEnum;
import cn.homyit.enums.ScoreRecordTypesEnum;
import cn.homyit.exception.BizException;
import cn.homyit.log.SystemLog;
import cn.homyit.service.*;
import cn.homyit.utils.BitMapClient;
import cn.homyit.utils.RedisCache;
import cn.homyit.utils.UserUtils;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static cn.homyit.constant.RedisConstants.DAILY_CLOCK_KEY;
import static cn.homyit.constant.RedisConstants.USER_CACHE_KEY;
import static cn.homyit.enums.ScoreRecordTypesEnum.PENALTY_OF_POINTS_FOR_VIOLATIONS;

/**
 * @program: graduate-website
 * @description: 用户业务接口
 * @author: Charon
 * @create: 2023-03-25 17:24
 **/
@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
    private UserService userService;
    private QrCodeService qrCodeService;
    private UserActivityService userActivityService;
    private ActivityService activityService;
    private ScoreRecordsService scoreRecordsService;
    private BitMapClient bitMapClient;
    private RoleService roleService;
    private RedisCache redisCache;
    private PasswordEncoder passwordEncoder;

    @GetMapping("/test")
    @PreAuthorize("hasAuthority('system:admin')")
    public Result<?> test() {
        return Result.success();
    }

    @PostMapping("/login")
    @SystemLog(businessName = "用户登录")
    public Result<Map<String, String>> login(@RequestBody @Validated LoginUserDto userDto) {
        Map<String, String> res = userService.login(userDto);
        return Result.success(res);
    }

    @PostMapping("/logout")
    @SystemLog(businessName = "退出登录")
    public Result<?> logout() {
        userService.logout(null);
        return Result.success();
    }

    @GetMapping()
    @SystemLog(businessName = "获取个人信息")
    public Result<UserInfoDto> getPersonalInfo() {
        UserInfoDto userInfoDto = userService.getPersonalInfo();
        return Result.success(userInfoDto);
    }

    @PutMapping()
    @SystemLog(businessName = "更新个人信息")
    public Result<?> updateUserInfo(@RequestBody @Validated UserInfoDto userInfoDto) {
        userService.updateUserInfo(userInfoDto);
        return Result.success();
    }

    @PutMapping("/password")
    @SystemLog(businessName = "修改密码")
    public Result<?> updateUserPassword(@RequestBody @Validated UserPasswordDto userPasswordDto) {
        userService.updateUserPassword(userPasswordDto);
        //强制t出
        userService.logout(null);
        return Result.success();
    }

    @GetMapping("/personal-qr")
    @SystemLog(businessName = "获取个人二维码")
    public Result<?> getPersonalQr(HttpServletResponse response) {
        String uuid = userService.getQrUUID();
        qrCodeService.createCodeToStream(uuid, response);
        return Result.success();
    }

    @PostMapping("/getScoreRecords")
    @SystemLog(businessName = "获取积分流水")
    public Result<?> getScoreRecords(@RequestBody @Validated ScoreRecordsPageDto scoreRecordsPageDto) {
        PageVo<?> scoreRecordPageVo = scoreRecordsService.getScoreRecords(scoreRecordsPageDto);
        return Result.success(scoreRecordPageVo);
    }

    @GetMapping("/scoreRecordsTypes")
    @SystemLog(businessName = "获取积分流水种类及其id")
    public Result<?> getScoreRecordsTypes() {
        return Result.success(ScoreRecordTypesEnum.values());
    }


    @PostMapping("/activity/list")
    @SystemLog(businessName = "个人活动参与信息分页查询")
    public Result<?> userActivityList(@RequestBody @Validated UserActivityPageDto userActivityDto) {
        //根据活动名称检索到活动id列表
        String activityName = userActivityDto.getActivityName();
        if (StrUtil.isNotBlank(activityName)) {
            userActivityDto.setActivityIds(activityService.getActivityIdsByName(activityName));
        }
        //分页查询出参与活动的基本信息
        PageVo<UserActivity> userActivityPageVo = userActivityService.userActivityList(userActivityDto);
        return Result.success(userActivityPageVo);
    }

    @GetMapping("/activity/{id}")
    @SystemLog(businessName = "根据活动id查询用户的参与情况")
    public Result<?> getUserActivity(@PathVariable Long id) {
        UserActivity userActivity = Optional.ofNullable(userActivityService.lambdaQuery()
                .eq(UserActivity::getActivityId, id)
                .eq(UserActivity::getUserId, UserUtils.getUser().getUser().getId())
                .one()).orElseThrow(() -> new BizException(ResultCodeEnum.NOT_SIGN));

        return Result.success(userActivity);
    }

    @PostMapping("/dailyClockIn")
    @SystemLog(businessName = "每日签到打卡")
    public Result<?> saveDailyClockIn(@RequestParam String url) {
        userService.saveDailyClockIn(url);
        return Result.success();
    }

    @PostMapping("/dailyClockIn/{yearMonth}")
    @SystemLog(businessName = "根据月份获取当月的打卡情况")
    public Result<?> getDailyClockIn(@PathVariable("yearMonth") String yearMonth) {
        Map<Integer, Boolean> map = bitMapClient.getClockIn(DAILY_CLOCK_KEY, yearMonth);
        return Result.success(map);
    }

    @GetMapping("/roleList")
    @SystemLog(businessName = "获取所有角色及其对应的id")
    public Result<?> roleList() {
        List<Role> roleList = roleService.list();
        return Result.success(roleList);
    }

    @DeleteMapping("/disable/{ids}")
    @PreAuthorize("hasAuthority('system:admin')")
    @SystemLog(businessName = "批量停用用户")
    public Result<?> disableUsers(@PathVariable List<Long> ids) {
        userService.disableUsers(ids);
        return Result.success();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('system:admin')")
    @SystemLog(businessName = "管理员查看用户详细信息")
    public Result<?> getUserById(@PathVariable Long id) {
        User user = userService.getById(id);
        return Result.success(user);
    }

    @PutMapping("/updateUser")
    @PreAuthorize("hasAuthority('system:admin')")
    @SystemLog(businessName = "管理员修改用户信息")
    public Result<?> updateUser(@RequestBody @Validated User user) {
        if (StrUtil.isNotBlank(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userService.updateById(user);
        userService.logout(user.getId());
        redisCache.deleteObject(USER_CACHE_KEY + user.getId());
        return Result.success();
    }

    @DeleteMapping("/clearScore/{id}")
    @PreAuthorize("hasAuthority('system:admin')")
    @SystemLog(businessName = "扣减用户积分")
    public Result<?> clearScore(@PathVariable Long id, @RequestParam @NotNull Long score) {
        User user = userService.getById(id);
        scoreRecordsService.insertRecord(user.getScore(), score,
                null, PENALTY_OF_POINTS_FOR_VIOLATIONS, user.getId());
        score = user.getScore() - score;
        userService.lambdaUpdate()
                .eq(User::getId, id)
                .set(User::getScore, score < 0 ? 0 : score)
                .update();
        //删除缓存
        redisCache.deleteObject(USER_CACHE_KEY + id);
        return Result.success();
    }

    @PostMapping("/list")
    @SystemLog(businessName = "分页查询用户信息")
    public Result<?> userList(@RequestBody @Validated UserPageDto userPageDto) {
        PageVo<?> pageVo = userService.userList(userPageDto);
        return Result.success(pageVo);
    }

    @GetMapping("/listByScore")
    @SystemLog(businessName = "查询积分榜前20名用户")
    public Result<?> listByScore() {
        Page<User> page = new Page<>(1, 20);
        userService.lambdaQuery()
                .orderByDesc(User::getScore)
                .eq(User::getRoleId, 3)
                .select(User::getName, User::getFromClass, User::getScore)
                .page(page);
        return Result.success(JSONUtil.parse(page.getRecords()));
    }


}
