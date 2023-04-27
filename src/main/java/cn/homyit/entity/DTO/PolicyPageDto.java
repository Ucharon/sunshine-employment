package cn.homyit.entity.DTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @program: graduate-website
 * @description:
 * @author: Charon
 * @create: 2023-04-08 13:44
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class PolicyPageDto extends PageDto {

    /**
     * 政策名称
     */
    private String name;

    /**
     * 政策类型
     */
    private Integer directId;
}
