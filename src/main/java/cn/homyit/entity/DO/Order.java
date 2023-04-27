package cn.homyit.entity.DO;

import cn.homyit.enums.OrderStatusEnum;
import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * 
 * @TableName tb_order
 */
@TableName(value ="tb_order")
@Data
public class Order implements Serializable {
    /**
     * 订单号
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 订单状态（0代表未使用，1代表已使用，2代表已退款)
     */
    private OrderStatusEnum statusEnum;

    /**
     * 使用时间
     */
    private LocalDateTime usedTime;

    /**
     * 退款时间
     */
    private LocalDateTime refundTime;

    /**
     * 删除标志（0代表未删除，1代表已删除）
     */
    @JsonIgnore
    private Integer delFlag;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}