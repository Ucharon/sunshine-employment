package cn.homyit.entity.DO;

import cn.homyit.enums.AppointStatusEnum;
import cn.homyit.enums.AppointTypeEnum;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.Null;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 预约表
 *
 * @TableName tb_appoint
 */
@TableName(value = "tb_appoint", autoResultMap = true)
@Data
public class Appoint implements Serializable {
    /**
     * 预约信息id
     */
    @TableId
    private Long id;

    /**
     * 学生id
     */
    @Null(message = "请勿填写该字段")
    private Long stuId;

    /**
     * 学生姓名
     */
    @TableField(exist = false)
    private String stuName;

    /**
     * 学生班级
     */
    @TableField(exist = false)
    private String fromClass;

    /**
     * 老师id
     */
    private Long teaId;

    /**
     * 老师姓名
     */
    @TableField(exist = false)
    private String teaName;

    /**
     * 老师电话
     */
    @TableField(exist = false)
    private String teaPhonenumber;

    /**
     * 预约类型id
     */
    private AppointTypeEnum appointTypeEnum;

    /**
     * 简历文件名称（如果有的话）
     */
    private String resumeFile;

    /**
     * 预约地址
     */
    private String address;

    /**
     * 内容
     */
    private String content;

    /**
     * 预约时间段
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> appointTime;

    /**
     * 预约状态
     */
    @Null(message = "请勿填写该字段")
    private AppointStatusEnum appointStatusEnum;

    /**
     * 预约成功照片
     */
    private String imgUrl;

    /**
     * 创建时间
     */
    @Null(message = "请勿填写该字段")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Null(message = "请勿填写该字段")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 删除标志（0代表未删除，1代表已删除）
     */
    @JsonIgnore
    @Null(message = "请勿填写该字段")
    private Integer delFlag;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}