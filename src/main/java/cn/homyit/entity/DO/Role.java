package cn.homyit.entity.DO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * 角色表
 * @TableName sys_role
 */
@TableName(value ="sys_role")
@Data
public class Role implements Serializable {
    /**
     * 角色id
     */
    @TableId
    private Integer id;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 删除标志（0代表未删除，1代表已删除）
     */
    @JsonIgnore
    private Integer delFlag;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}