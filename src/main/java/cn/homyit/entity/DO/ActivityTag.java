package cn.homyit.entity.DO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 活动标签表
 * @TableName tb_activity_tag
 */
@TableName(value ="tb_activity_tag")
@Data
public class ActivityTag implements Serializable {
    /**
     * 标签id
     */
    @TableId
    private Integer id;

    /**
     * 标签名称
     */
    private String name;

    /**
     * 删除标志（0代表未删除，1代表已删除）
     */
    private Integer delFlag;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}