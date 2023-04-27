package cn.homyit.entity.DO;

import cn.homyit.enums.RoleModelTypeEnum;
import cn.homyit.utils.RegexPatterns;
import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.Pattern;

/**
 * 
 * @TableName tb_role_model
 */
@TableName(value ="tb_role_model")
@Data
public class RoleModel implements Serializable {
    /**
     * 红榜id
     */
    @TableId
    private Long id;

    /**
     * 红榜类型
     */
    private RoleModelTypeEnum roleModelTypeEnum;

    /**
     * 红榜名称
     */
    private String name;

    /**
     * 学生名称
     */
    private String stuName;

    /**
     * 手机号
     */
    @Pattern(regexp = RegexPatterns.PHONE_REGEX)
    private String phonenumber;

    /**
     * 个人感想
     */
    private String feeling;

    /**
     * 获奖图片
     */
    private String imgUrl;

    /**
     * 删除标志（0代表未删除，1代表已删除）
     */
    @JsonIgnore
    private Integer delFlag;

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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}