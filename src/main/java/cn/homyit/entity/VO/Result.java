package cn.homyit.entity.VO;


import cn.homyit.enums.ResultCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {

    private Integer code;
    private String message;
    private T data;

    //不带数据的构造方法
    public Result(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 带数据成功返回
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(ResultCodeEnum.SUCCESS.getCode(),
                ResultCodeEnum.SUCCESS.getDesc(), data);
    }

    /**
     * 未携带数据成功返回
     *
     * @param <T>
     * @return
     */
    public static <T> Result<T> success() {
        return new Result(ResultCodeEnum.SUCCESS.getCode(),
                ResultCodeEnum.SUCCESS.getDesc());
    }

    /**
     * 通用错误返回
     *
     * @param exceptionCodeEnum
     * @return
     */
    public static <T> Result<T> error(ResultCodeEnum exceptionCodeEnum) {
        return new Result(exceptionCodeEnum.getCode(),
                exceptionCodeEnum.getDesc());
    }

    /**
     * 通用错误返回
     *
     * @param exceptionCodeEnum
     * @param msg
     * @return
     */
    public static <T> Result<T> error(ResultCodeEnum exceptionCodeEnum, String msg) {
        return new Result(exceptionCodeEnum.getCode(), msg);
    }

    /**
     * 通用错误返回
     *
     * @param exceptionCodeEnum
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Result<T> error(ResultCodeEnum exceptionCodeEnum, T data) {
        return new Result<>(exceptionCodeEnum.getCode(),
                exceptionCodeEnum.getDesc(), data);
    }

    public boolean isSuccess() {
        return ResultCodeEnum.SUCCESS.getCode().equals(code);
    }

}
