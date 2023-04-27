package cn.homyit.entity.DTO;

import cn.homyit.enums.RoleModelTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @program: graduate-website
 * @description:
 * @author: Charon
 * @create: 2023-04-07 17:07
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class RolePageDto extends PageDto {

    /**
     * 红榜类型
     */
    private RoleModelTypeEnum roleModelTypeEnum;

    /**
     * 红榜名称
     */
    private String name;

    /**
     * 学生名称
     */
    private String stuName;


}
