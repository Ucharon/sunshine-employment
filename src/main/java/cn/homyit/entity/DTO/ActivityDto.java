package cn.homyit.entity.DTO;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @program: graduate-website
 * @description:
 * @author: Charon
 * @create: 2023-03-31 10:19
 **/
@Data
public class ActivityDto {

    /**
     * 活动名称
     */
    @NotNull(message = "活动名称不能为空")
    private String name;

    /**
     * 主办用户id
     */
    @NotNull(message = "举办老师不能为空")
    private Long userId;

    /**
     * 活动标签id
     */
    @NotNull(message = "活动标签不能为空")
    private Integer tagId;

    /**
     * 对应方向id
     */
    @NotNull(message = "活动方向不能为空")
    private Integer directId;

    /**
     * 缩略图
     */
    private String thumbnail;

    /**
     * 积分数
     */
    @NotNull(message = "积分数不能为空")
    private Double score;

    /**
     * 地点
     */
    @NotNull(message = "地点不能为空")
    private String location;

    /**
     * 详细信息
     */
    private String details;

    /**
     * 报名截止日期
     */
    @NotNull(message = "报名截止日期不能为空")
    private LocalDateTime signEnd;

    /**
     * 活动开始时间
     */
    @NotNull(message = "活动开始时间不能为空")
    private LocalDateTime activityBegin;

    /**
     * 活动持续时间
     */
    @NotNull(message = "活动持续时间不能为空")
    private Integer standing;

}
