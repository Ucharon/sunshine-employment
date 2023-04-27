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
public enum AppointStatusEnum {

    UNPROCESSED(0, "老师未处理，等待老师同意"),
    TEACHER_AGREES(1, "老师同意，请线下交流"),
    END_OF_APPOINTMENT(2, "预约结束"),
    TEACHER_REJECT(3, "老师拒绝预约");

    @EnumValue
    private final Integer statusCode;
    private final String statusName;

    @JsonCreator
    public static AppointStatusEnum getByCode(Integer code) {
        return Arrays.stream(AppointStatusEnum.values())
                .filter(appointStatusEnum -> appointStatusEnum.getStatusCode().equals(code))
                .findFirst()
                .orElse(null);
    }
}
