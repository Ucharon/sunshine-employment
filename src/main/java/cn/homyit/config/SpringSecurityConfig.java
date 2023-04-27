package cn.homyit.config;

import cn.homyit.filter.TokenAuthenticationFilter;
import cn.homyit.handler.FilterChainExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.Arrays;

/**
 * @program: graduate-website
 * @description: SpringSecurity配置
 * @author: Charon
 * @create: 2023-03-25 16:03
 **/
@Configuration
@EnableWebSecurity                                  //添加security过滤器
@EnableGlobalMethodSecurity(prePostEnabled = true)  //启用方法级别的权限认证
public class SpringSecurityConfig {

    @Resource
    private TokenAuthenticationFilter tokenAuthenticationFilter;
    @Resource
    private FilterChainExceptionHandler filterChainExceptionHandler;

    /**
     * 密码明文加密方式配置
     *
     * @return
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /**
     * 获取AuthenticationManager（认证管理器），登录时认证使用
     *
     * @param authenticationConfiguration
     * @return
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * HttpSecurity配置
     *
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                //基于token，不需要csrf
                .csrf().disable()
                //基于token，不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests()

                // 对于登录接口 允许匿名访问
                .antMatchers("/user/login", "/wx/checkSignature")
                .anonymous()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated()
                //开发中，暂时不需要鉴权认证，如需要，请注释掉下方代码，并开启上方代码开启鉴权认证
                //.anyRequest().permitAll().and()
                .and().cors()
                .configurationSource(corsConfigurationSource())

                //把token校验过滤器添加到过滤器链中
                .and().addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(filterChainExceptionHandler, TokenAuthenticationFilter.class)
                .build();
    }

    /**
     * 配置跨源访问(CORS)
     *
     * @return
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setMaxAge(Duration.ofHours(1));
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
