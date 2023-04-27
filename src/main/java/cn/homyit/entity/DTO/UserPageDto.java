package cn.homyit.entity.DTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @program: graduate-website
 * @description:
 * @author: Charon
 * @create: 2023-04-07 09:56
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class UserPageDto extends PageDto {

    /**
     * 用户名-学号工号
     */
    private String userName;

    /**
     * 姓名
     */
    private String name;

    /**
     * 角色id
     */
    private Integer roleId;

    /**
     * 班级
     */
    private String fromClass;

    /**
     * 账号状态（0正常 1停用）
     */
    private Integer status;

    /**
     * 第一方向
     */
    private Integer firstDirect;

    /**
     * 第二方向
     */
    private Integer secondDirect;

}
