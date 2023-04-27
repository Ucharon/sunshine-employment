package cn.homyit.controller;

import cn.homyit.utils.CheckUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @program: graduate-website
 * @description:
 * @author: Charon
 * @create: 2023-04-04 15:42
 **/
@RestController
@RequestMapping("/wx")
public class WXController {

    @GetMapping("/checkSignature")
    public void checkSignature(HttpServletRequest request, HttpServletResponse response) {
        try (PrintWriter out = response.getWriter()) {
            // 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
            String signature = request.getParameter("signature");
            // 时间戳
            String timestamp = request.getParameter("timestamp");
            // 随机数
            String nonce = request.getParameter("nonce");
            // 随机字符串
            String echostr = request.getParameter("echostr");
            if (CheckUtils.checkSignature(signature, timestamp, nonce)) {
                out.print(echostr);
                out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
