package cn.homyit.entity.DTO;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @program: graduate-website
 * @description: 登录Dto
 * @author: Charon
 * @create: 2023-03-25 17:27
 **/
@Data
public class LoginUserDto {

    /**
     * 用户名-学号工号
     */
    @NotNull(message = "账号不能为空")
    private String userName;

    /**
     * 密码
     */
    @NotNull(message = "用户密码不能为空")
    private String password;
}
