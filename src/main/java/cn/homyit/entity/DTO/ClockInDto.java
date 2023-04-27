package cn.homyit.entity.DTO;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @program: graduate-website
 * @description:
 * @author: Charon
 * @create: 2023-03-31 21:49
 **/
@Data
public class ClockInDto {

    @NotBlank(message = "图片不能为空")
    private String imgUrl;
    @NotNull(message = "文章id不能为空")
    private Long activityId;
}
