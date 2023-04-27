package cn.homyit.entity.DO;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * 用户-活动关联表
 * @TableName tb_user_activity
 */
@TableName(value ="tb_user_activity")
@Data
public class UserActivity implements Serializable {
    /**
     * 用户活动id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 学生姓名
     */
    @TableField(exist = false)
    private String name;

    /**
     * 学生班级
     */
    @TableField(exist = false)
    private String fromClass;

    /**
     * 学生积分
     */
    @TableField(exist = false)
    private Long score;

    /**
     * 活动id
     */
    private Long activityId;

    /**
     * 活动名称
     */
    @TableField(exist = false)
    private String activityName;

    /**
     * 活动是否完成（0代表未完成，1代表已完成）
     */
    private Integer isComplete;

    /**
     * 打卡图片
     */
    private String clockImg;

    /**
     * 打卡时间
     */
    private LocalDateTime clockTime;

    /**
     * 发表时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 是否被取消资格
     */
    private Integer status;

    /**
     * 删除标志（0代表未删除，1代表已删除）
     */
    @JsonIgnore
    private Integer delFlag;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}