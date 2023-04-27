package cn.homyit.entity.DO;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

/**
 * 方向表
 * @TableName tb_direct
 */
@TableName(value ="tb_direct")
@Data
public class Direct implements Serializable {
    /**
     * 方向id
     */
    @TableId
    private Integer id;

    /**
     * 方向名称
     */
    private String name;

    /**
     * 前端无用字段
     */
    @TableField(exist = false)
    private Boolean isChecked = false;

    /**
     * 删除标志（0代表未删除，1代表已删除）
     */
    @JsonIgnore
    private Integer delFlag;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}