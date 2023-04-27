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
 * @create: 2023-04-07 16:44
 **/
@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum RoleModelTypeEnum {

    GRADUATE_MODEL(1, "考研红榜"),
    PEER_ROLE_MODEL(2, "朋辈榜样"),
    EMPLOYMENT_MODEL(3, "就业榜样");

    @EnumValue
    private final Integer typeCode;
    private final String typeName;

    @JsonCreator
    public static RoleModelTypeEnum getByCode(Integer code) {
        return Arrays.stream(RoleModelTypeEnum.values())
                .filter(roleModelTypeEnum -> roleModelTypeEnum.getTypeCode().equals(code))
                .findFirst()
                .orElse(null);
    }

}
