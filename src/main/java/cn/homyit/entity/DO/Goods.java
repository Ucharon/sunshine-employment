package cn.homyit.entity.DO;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品表
 * @TableName tb_goods
 */
@TableName(value ="tb_goods", autoResultMap = true)
@Data
public class Goods implements Serializable {
    /**
     * 商品id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 详细信息
     */
    private String details;

    /**
     * 积分
     */
    private Long score;

    /**
     * 库存
     */
    private Long stock;

    /**
     * 缩略图地址
     */
    private String thumbnail;

    /**
     * 详情图
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> detailedPicture;

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
     * 商品状态字段（0表示未下架，1表示已下架）
     */
    @JsonIgnore
    private Integer status;

    /**
     * 删除标志（0代表未删除，1代表已删除）
     */
    @JsonIgnore
    private Integer delFlag;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public static Goods getDisableGoods() {
        Goods goods = new Goods();
        goods.setName("商品已下架");
        goods.setThumbnail("");
        return goods;
    }
}