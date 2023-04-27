package cn.homyit.exception;


import cn.homyit.enums.ResultCodeEnum;
import lombok.Getter;

/**
 * 业务异常
 * biz是business（业务）缩写
 */
@Getter
public class BizException extends RuntimeException {

    private final ResultCodeEnum error;

    /**
     * 构造器，有时我们需要将第三方异常转为自定义异常抛出，但又不想丢失原来的异常信息，此时可以传入cause
     *
     * @param error
     * @param cause
     */
    public BizException(ResultCodeEnum error, Throwable cause) {
        super(cause);
        this.error = error;
    }

    /**
     * 构造器，只传入错误枚举
     *
     * @param error
     */
    public BizException(ResultCodeEnum error) {
        this.error = error;
    }

}
