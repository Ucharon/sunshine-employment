package cn.homyit.entity.DTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @program: graduate-website
 * @description:
 * @author: Charon
 * @create: 2023-04-05 16:57
 **/

@EqualsAndHashCode(callSuper = true)
@Data
public class ActivityClockInInfoPageDto extends PageDto{

    @NotNull(message = "活动id不能为空")
    private Long activityId;


    private Integer isComplete;
}
