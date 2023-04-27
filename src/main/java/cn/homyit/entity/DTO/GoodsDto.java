package cn.homyit.entity.DTO;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @program: graduate-website
 * @description:
 * @author: Charon
 * @create: 2023-03-28 17:49
 **/
@Data
public class GoodsDto {

    /**
     * 商品名称
     */
    @NotNull(message = "名称不能为空")
    private String name;

    /**
     * 详细信息
     */
    private String details;

    /**
     * 积分
     */
    @NotNull(message = "积分不能为空")
    private Double score;

    /**
     * 库存
     */
    @NotNull(message = "库存不能为空")
    private Long stock;

    /**
     * 缩略图地址
     */
    private String thumbnail;

    /**
     * 详情图
     */
    private List<String> detailedPicture;

}
