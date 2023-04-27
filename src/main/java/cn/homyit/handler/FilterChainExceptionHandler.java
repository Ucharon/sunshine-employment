package cn.homyit.handler;

import cn.homyit.entity.VO.Result;
import cn.homyit.exception.BizException;
import cn.homyit.utils.WebUtils;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 主要捕获token验证中的业务异常
 */
@Component
@Slf4j
public class FilterChainExceptionHandler extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        Result<Object> result = null;
        try {
            filterChain.doFilter(request, response);
        } catch (BizException e) {
            log.error(e.getError().getDesc());
            result = Result.error(e.getError());
            String jsonString = JSON.toJSONString(result);
            WebUtils.renderString(response, jsonString);
        }
    }
}
