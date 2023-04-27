package cn.homyit.entity.VO;

import cn.homyit.entity.DO.Order;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @program: graduate-website
 * @description:
 * @author: Charon
 * @create: 2023-03-29 14:49
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class OrderPageVo extends Order {

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 前端无用字段
     */
    private Boolean isDisabled = false;

    /**
     * 缩略图地址
     */
    private String thumbnail;

}
