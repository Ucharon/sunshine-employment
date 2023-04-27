package cn.homyit.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @program: graduate-website
 * @description:
 * @author: Charon
 * @create: 2023-04-01 16:13
 **/

@AllArgsConstructor
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ScoreRecordTypesEnum {

    ACTIVITY_IN(1, "活动星币到账"),
    PENALTY_OF_POINTS_FOR_VIOLATIONS(2, "违规扣减星币"),
    BUY_GOODS(3, "购买商品"),
    DAILY_CLOCK_IN(4, "每日打卡星币到账");

    @EnumValue
    private final Integer typeCode;
    private final String typeDesc;

    @JsonCreator
    public static ScoreRecordTypesEnum getByCode(Integer code) {
        return Arrays.stream(ScoreRecordTypesEnum.values())
                .filter(scoreRecordTypesEnum -> scoreRecordTypesEnum.getTypeCode().equals(code))
                .findFirst().orElse(null);
    }
}
