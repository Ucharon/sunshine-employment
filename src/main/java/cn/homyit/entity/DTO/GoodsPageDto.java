package cn.homyit.entity.DTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @program: graduate-website
 * @description: 接收商品分页信息
 * @author: Charon
 * @create: 2023-03-28 16:37
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class GoodsPageDto extends PageDto {

    /**
     * 商品名称模糊检索
     */
    private String name;


}
