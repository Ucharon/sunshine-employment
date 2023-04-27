package cn.homyit.entity.DTO;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;

import static cn.homyit.utils.RegexPatterns.PHONE_REGEX;

/**
 * @program: graduate-website
 * @description:
 * @author: Charon
 * @create: 2023-03-26 16:55
 **/
@Data
public class UserInfoDto {

    /**
     * 主键-id
     */
    @Null(message = "不要尝试修改id!")
    private Long id;

    /**
     * 用户名-学号工号
     */
    @Null(message = "不要尝试修改你的学号或工号!")
    private String userName;

    /**
     * 姓名
     */
    @Null(message = "不要尝试修改你的姓名!")
    private String name;

    /**
     * 角色id
     */
    @Null(message = "不要尝试修改你的角色！")
    private Integer roleId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 第一方向
     */
    private Integer firstDirect;

    /**
     * 第一方向名称
     */
    private String firstDirectName;

    /**
     * 第二方向
     */
    private Integer secondDirect;

    /**
     * 第二方向名称
     */
    private String secondDirectName;

    /**
     * 积分
     */
    @Null(message = "不要擅自修改积分！")
    private Long score;

    /**
     * 邮箱
     */
    @Email
    private String email;

    /**
     * 手机号
     */
    @Pattern(regexp = PHONE_REGEX, message = "手机号格式错误")
    private String phonenumber;

    /**
     * 班级
     */
    @Null(message = "不要擅自修改班级！")
    private String fromClass;

    /**
     * 简历文件
     */
    private String resumeFile;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 额外字段-参与活动数
     */
    private Long numOfAct;

    /**
     * 额外字段-商品购买数
     */
    private Long numOfBuy;
}
