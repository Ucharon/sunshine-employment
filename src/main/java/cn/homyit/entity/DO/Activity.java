package cn.homyit.entity.DO;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 活动表
 *
 * @TableName tb_activity
 */
@TableName(value = "tb_activity")
@Data
public class Activity implements Serializable {
    /**
     * 活动id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 活动名称
     */
    private String name;

    /**
     * 主办用户id
     */
    private Long userId;

    /**
     * 主办用户姓名
     */
    @TableField(exist = false)
    private String userName;

    /**
     * 活动标签id
     */
    private Integer tagId;

    /**
     * 活动标签名称
     */
    @TableField(exist = false)
    private String tagName;

    /**
     * 对应方向id
     */
    private Integer directId;

    /**
     * 对应方向名称
     */
    @TableField(exist = false)
    private String directName;

    /**
     * 缩略图
     */
    private String thumbnail;

    /**
     * 积分数
     */
    private Long score;

    /**
     * 地点
     */
    private String location;

    /**
     * 详细信息
     */
    private String details;

    /**
     * 报名人数
     */
    private Integer signNums;

    /**
     * 完成人数
     */
    @TableField(exist = false)
    private Integer completeNum;

    /**
     * 报名截止日期
     */
    private LocalDateTime signEnd;

    /**
     * 活动开始时间
     */
    private LocalDateTime activityBegin;

    /**
     * 活动结束时间
     */
    private LocalDateTime activityEnd;

    /**
     * 是否结束
     */
    @TableField(exist = false)
    private Boolean isEnd;

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
     * 活动是否下架
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