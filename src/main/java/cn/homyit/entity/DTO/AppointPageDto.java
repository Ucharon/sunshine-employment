package cn.homyit.entity.DTO;

import cn.homyit.enums.AppointStatusEnum;
import cn.homyit.enums.AppointTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Null;

/**
 * @program: graduate-website
 * @description:
 * @author: Charon
 * @create: 2023-04-06 14:13
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class AppointPageDto extends PageDto {

    /**
     * 学生id
     */
    @Null(message = "请勿填写该字段")
    private Long stuId;

    /**
     * 老师id
     */
    @Null(message = "请勿填写该字段")
    private Long teaId;

    /**
     * 预约类型id
     */
    private AppointTypeEnum appointTypeEnum;

    /**
     * 预约状态
     */
    private AppointStatusEnum appointStatusEnum;


}
