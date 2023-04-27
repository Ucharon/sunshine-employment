package cn.homyit.service;

import cn.homyit.entity.DO.User;
import cn.homyit.entity.DTO.LoginUserDto;
import cn.homyit.entity.DTO.UserInfoDto;
import cn.homyit.entity.DTO.UserPageDto;
import cn.homyit.entity.DTO.UserPasswordDto;
import cn.homyit.entity.VO.PageVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
* @author charon
* @description 针对表【sys_user(用户表)】的数据库操作Service
* @createDate 2023-03-25 17:21:07
*/
public interface UserService extends IService<User> {

    Map<String, String> login(LoginUserDto userDto);

    UserInfoDto getPersonalInfo();

    void updateUserInfo(UserInfoDto userInfoDto);

    void logout(Long id);

    String getQrUUID();

    Long userUuidGetUserId(String uuid);

    void updateUserPassword(UserPasswordDto userPasswordDto);

    void disableUsers(List<Long> ids);

    void saveDailyClockIn(String url);

    PageVo<?> userList(UserPageDto userPageDto);

}
