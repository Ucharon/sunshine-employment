package cn.homyit.log;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
@Aspect
@Slf4j
public class LogAspect {

    @Pointcut("@annotation(cn.homyit.log.SystemLog)")
    public void pt() {

    }

    @Around("pt()")
    public Object pointLog(ProceedingJoinPoint joinPoint) throws Throwable {

        Object ret;
        long startTime = System.currentTimeMillis();
        try {
            handleBefore(joinPoint);
            ret = joinPoint.proceed();
            handleAfter(ret);
        } finally {
            //结束后换行
            log.info("time              : {}ms", (System.currentTimeMillis() - startTime));
            log.info("=============End=============" + System.lineSeparator());
        }

        return ret;
    }


    private void handleAfter(Object ret) {
        //打印出参
        log.info("Response          : {}", JSON.toJSONString(ret));
    }

    private void handleBefore(ProceedingJoinPoint joinPoint) {
        ServletRequestAttributes requestAttributes
                = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        //获取被增强方法上的增强的注解对象
        SystemLog systemLog = getSystemLog(joinPoint);

        log.info("=============Start=============");
        //打印请求URL
        log.info("URL               : {}", request.getRequestURL());
        //打印描述信息
        log.info("BusinessName      : {}", systemLog.businessName());
        //打印Http Method
        log.info("Http Method       : {}", request.getMethod());
        //打印调用controller到全路径以及执行方法
        log.info("Class Method      : {}.{}", joinPoint.getSignature().getDeclaringType(),
                ((MethodSignature) joinPoint.getSignature()).getName());
        //打印请求的IP
        log.info("IP                : {}", request.getRemoteHost());

        //获取并请求入参
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();

        log.info("Request Args      : {}", JSON.toJSONString(getMethodParameter(method, joinPoint.getArgs())));
    }

    /**
     * 获取方法的参数
     *
     * @param method
     * @param args
     * @return
     */
    private Map<String, Object> getMethodParameter(Method method, Object[] args) {
        Map<String, Object> map = new HashMap<>();
        LocalVariableTableParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
        //方法的形参名称
        String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
        for (int i = 0; i < Objects.requireNonNull(parameterNames).length; i++) {
            //敏感词汇过滤
            if ("userDto".equals(parameterNames[i])
                    || "file".equals(parameterNames[i])
                    || "files".equals(parameterNames[i])
                    || "res".equals(parameterNames[i])
                    || "response".equals(parameterNames[i])
                    || "request".equals(parameterNames[i])
                    || "userInfoDto".equals(parameterNames[i])) {
                map.put(parameterNames[i], "受限的支持类型");
            } else {
                map.put(parameterNames[i], args[i]);
            }
        }

        return map;
    }


    private SystemLog getSystemLog(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod().getAnnotation(SystemLog.class);
    }
}
