package cn.homyit.entity.DTO;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static cn.homyit.utils.RegexPatterns.PASSWORD_REGEX;

/**
 * @program: graduate-website
 * @description:
 * @author: Charon
 * @create: 2023-03-28 14:22
 **/
@Data
public class UserPasswordDto {

    @Pattern(regexp = PASSWORD_REGEX, message = "密码格式错误！密码是4~32位的字母、数字、下划线")
    @NotNull(message = "密码不能为空")
    private String originalPassword;

    @Pattern(regexp = PASSWORD_REGEX, message = "密码格式错误！密码是4~32位的字母、数字、下划线")
    @NotNull(message = "密码不能为空")
    private String newPassword;
}
