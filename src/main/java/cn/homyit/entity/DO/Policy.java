package cn.homyit.entity.DO;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 
 * @TableName tb_policy
 */
@TableName(value ="tb_policy")
@Data
public class Policy implements Serializable {
    /**
     * 政策id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 政策名称
     */
    @NotBlank(message = "政策名不能为空")
    private String name;

    /**
     * 文件名称
     */
    @NotBlank(message = "文件名不能为空")
    private String fileName;

    /**
     * 方向id
     */
    @NotNull(message = "方向不能为空")
    private Integer directId;

    /**
     * 方向名称
     */
    @TableField(exist = false)
    private String directName;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 删除标志（0代表未删除，1代表已删除）
     */
    private Integer delFlag;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}