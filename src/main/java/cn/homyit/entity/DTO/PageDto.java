package cn.homyit.entity.DTO;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @program: graduate-website
 * @description: 分页基本信息
 * @author: Charon
 * @create: 2023-03-28 16:36
 **/
@Data
public class PageDto {

    /**
     * 页号，一页大小
     */
    @NotNull(message = "分页信息为空")
    private Integer pageNum;
    @NotNull(message = "分页信息为空")
    private Integer pageSize;

}
