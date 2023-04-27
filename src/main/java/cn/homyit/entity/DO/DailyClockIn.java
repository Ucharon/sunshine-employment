package cn.homyit.entity.DO;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName tb_daily_clock_in
 */
@TableName(value ="tb_daily_clock_in")
@Data
public class DailyClockIn implements Serializable {
    /**
     * id
     */
    @TableId
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 姓名
     */
    @TableField(exist = false)
    private String stuName;

    /**
     * 班级
     */
    @TableField(exist = false)
    private String fromClass;

    /**
     * 学生积分
     */
    @TableField(exist = false)
    private Long score;

    /**
     * 打卡图片地址
     */
    private String img;

    /**
     * 发表时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 删除标志（0代表未删除，1代表已删除）
     */
    private Integer delFlag;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}