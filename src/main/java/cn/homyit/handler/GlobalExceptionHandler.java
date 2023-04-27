package cn.homyit.handler;


import cn.homyit.entity.VO.Result;
import cn.homyit.enums.ResultCodeEnum;
import cn.homyit.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

/**
 * 全局异常处理器
 */

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    //请求参数里的参数校验
    @ExceptionHandler(ConstraintViolationException.class)
    public Result<ResultCodeEnum> handleConstraintViolationException(ConstraintViolationException e) {
        //返回泛化的错误信息，比如“参数错误”
        return Result.error(ResultCodeEnum.ERROR_PARAM, e.getMessage());
    }

    /**
     * 字段校验异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        //从异常对象中拿到ObjectError对象
        String defaultMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return Result.error(ResultCodeEnum.LOGIN_INPUT_ERROR, defaultMessage);
    }

    /**
     * 业务异常统一处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(BizException.class)
    public Result handleBizException(BizException e) {
        log.info("exception             : {}", e.getError().getDesc());
        return Result.error(e.getError());
    }

    /**
     * 授权异常处理器
     * @param e
     * @return
     */
    @ExceptionHandler(AccessDeniedException.class)
    public Result handleAccessDeniedException(AccessDeniedException e) {
        log.info("权限不足！");
        return Result.error(ResultCodeEnum.FORBIDDEN);
    }

    /**
     * 认证异常处理器
     * @param e
     * @return
     */
    @ExceptionHandler(AuthenticationException.class)
    public Result authenticationExceptionHandler(AuthenticationException e) {
        log.info("用户名密码错误！");
        return Result.error(ResultCodeEnum.LOGIN_ERROR);
    }


    /**
     * 处理其他异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public Result exceptionHandler(Exception e) {
        log.error("未知异常！原因如下：", e);
        return Result.error(ResultCodeEnum.INTERNAL_SERVER_ERROR);
    }

}
