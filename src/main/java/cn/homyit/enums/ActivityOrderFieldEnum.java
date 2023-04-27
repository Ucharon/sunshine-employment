package cn.homyit.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @program: graduate-website
 * @description: 活动排序字段指定
 * @author: Charon
 * @create: 2023-03-30 14:03
 **/
@AllArgsConstructor
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ActivityOrderFieldEnum {

    ID(0, "id", "ID"),
    SCORE(1, "score", "积分"),
    SIGN_NUMS(2, "sign_nums", "报名人数"),
    SIGN_END_TIME(3, "sign_end", "报名截止时间"),
    CREATE_TIME(4, "create_time", "活动创建时间"),
    ACTIVITY_BEGIN(5, "activity_begin", "活动开始时间");

    /**
     * 字段代码和字段名
     */
    private final Integer fieldCode;
    @JsonIgnore
    private final String fieldName;
    private final String fieldDesc;

    @JsonCreator
    public static ActivityOrderFieldEnum getByCode(Integer code) {
        return Arrays.stream(ActivityOrderFieldEnum.values())
                .filter(activityOrderFieldEnum -> activityOrderFieldEnum.getFieldCode().equals(code))
                .findFirst()
                .orElse(ActivityOrderFieldEnum.CREATE_TIME);
    }
}
