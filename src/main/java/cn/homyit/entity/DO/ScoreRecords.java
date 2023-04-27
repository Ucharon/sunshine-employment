package cn.homyit.entity.DO;

import cn.homyit.enums.ScoreRecordTypesEnum;
import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName score_records
 */
@TableName(value ="tb_score_records")
@Data
public class ScoreRecords implements Serializable {
    /**
     * 商品订单或参与活动id号
     */
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 表示是什么种类的流水
     */
    private ScoreRecordTypesEnum type;

    /**
     * 操作前用户积分数
     */
    private Long scoreBefore;

    /**
     * 操作后用户积分数
     */
    private Long scoreAfter;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}