package cn.homyit.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum OrderStatusEnum {

    NOT_USE(0, "未使用"),
    ALREADY_US(1, "已使用"),
    REFUNDED(2, "已退款");

    @EnumValue
    private final Integer statusCode;
    private final String statusName;

}
