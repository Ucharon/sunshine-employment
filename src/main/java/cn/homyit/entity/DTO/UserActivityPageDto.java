package cn.homyit.entity.DTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Null;
import java.util.List;

/**
 * @program: graduate-website
 * @description:
 * @author: Charon
 * @create: 2023-04-01 10:39
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class UserActivityPageDto extends PageDto {

    /**
     * 活动状态查询-0未打卡、1已完成
     */
    private Integer isComplete;

    /**
     * 根据活动名称模糊查询
     */
    private String activityName;

    /**
     * 根据活动名称模糊检索到的活动id
     */
    @Null(message = "请勿填写该字段")
    private List<Long> activityIds;

    /**
     * 升序or降序（ture：升序 false：降序）
     */
    private Boolean orderMode = false;
}
