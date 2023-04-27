package cn.homyit.entity.DTO;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

/**
 * @program: graduate-website
 * @description:
 * @author: Charon
 * @create: 2023-03-29 14:00
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class OrderPageDto extends PageDto{

    /**
     * 订单状态（0代表未使用，1代表已使用，2代表已退款)
     */
    @Pattern(regexp = "[0-2]", message = "订单状态不符合要求")
    private String status;

    /**
     * 根据商品名称模糊检索
     */
    private String goodsName;

    /**
     * 下列必须为订单创建时间的范围
     */
    @Past(message = "不能为将来的日期")
    private LocalDateTime beginTime;

    @Past(message = "不能为将来的日期")
    private LocalDateTime endTime;
}
