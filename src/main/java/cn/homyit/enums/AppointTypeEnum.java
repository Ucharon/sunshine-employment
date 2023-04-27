package cn.homyit.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum AppointTypeEnum {

    RESUME_MAKING(1, "简历制作", 4L),
    MOCK_INTERVIEW(2, "模拟面试", 4L),
    WRITTEN_EXAMINATION(3, "笔试", 4L),
    EMPLOYMENT_INFORMATION(4, "就业信息", 4L);

    @EnumValue
    private final Integer typeCode;
    private final String typeName;
    private final Long score;

    @JsonCreator
    public static AppointTypeEnum getByCode(Integer code) {
        return Arrays.stream(AppointTypeEnum.values())
                .filter(appointTypeEnum -> appointTypeEnum.getTypeCode().equals(code))
                .findFirst()
                .orElse(null);
    }
}
