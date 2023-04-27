package cn.homyit.entity.DTO;

import cn.homyit.enums.ActivityOrderFieldEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @program: graduate-website
 * @description: 接收活动分页信息
 * @author: Charon
 * @create: 2023-03-30 13:54
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class ActivityPageDto extends PageDto{

    /**
     * 模糊检索活动名称
     */
    private String name;

    /**
     * 标签id
     */
    private Integer tagId;

    /**
     * 指定方向id
     */
    private Integer directId;

    /**
     * 模糊检索地点
     */
    private String locationName;

    /**
     * 老师id
     */
    private Long userId;

    /**
     * 排序字段指定
     */
    private ActivityOrderFieldEnum orderField = ActivityOrderFieldEnum.CREATE_TIME;

    /**
     * 升序or降序（ture：升序 false：降序）
     */
    private Boolean orderMode = false;

    /**
     * 指定该活动是否已经结束
     */
    private Integer isEnd;
}
