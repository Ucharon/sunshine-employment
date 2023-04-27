package cn.homyit.entity.DTO;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @program: graduate-website
 * @description:
 * @author: Charon
 * @create: 2023-04-02 18:49
 **/
@Data
public class ExcelUserDto {

    /**
     * 用户名-学号工号
     */
    @ExcelProperty("学号工号")
    private String userName;

    /**
     * 姓名
     */
    @ExcelProperty("姓名")
    private String name;

    /**
     * 角色id
     */
    @ExcelProperty("角色id")
    private Integer roleId;

    /**
     * 手机号
     */
    @ExcelProperty("手机号")
    private String phonenumber;

    /**
     * 班级
     */
    @ExcelProperty("班级")
    private String fromClass;
}
