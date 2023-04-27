package cn.homyit.entity.DO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * 用户表
 * @TableName sys_user
 */
@TableName(value ="sys_user")
@Data
public class User implements Serializable {
    /**
     * 主键-id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

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
     * 角色名称
     */
    @TableField(exist = false)
    private String roleName;

    /**
     * 积分
     */
    private Long score;

    /**
     * 密码
     */
    private String password;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phonenumber;

    /**
     * 班级
     */
    private String fromClass;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 账号状态（0正常 1停用）
     */
    private String status;

    /**
     * 删除标志（0代表未删除，1代表已删除）
     */
    private Integer delFlag;

    /**
     * 额外字段-参与活动数
     */
    @TableField(exist = false)
    private Long numOfAct;

    /**
     * 第一方向
     */
    private Integer firstDirect;

    /**
     * 第一方向名称
     */
    @TableField(exist = false)
    private String firstDirectName;

    /**
     * 第二方向
     */
    private Integer secondDirect;

    /**
     * 第二方向名称
     */
    @TableField(exist = false)
    private String secondDirectName;

    /**
     * 简历文件
     */
    private String resumeFile;

    /**
     * 额外字段-商品购买数
     */
    @TableField(exist = false)
    private Long numOfBuy;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}