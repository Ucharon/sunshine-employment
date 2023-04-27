package cn.homyit.config;

import cn.hutool.extra.qrcode.QrConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.awt.*;

/**
 * @program: graduate-website
 * @description: 二维码工具配置类
 * @author: Charon
 * @create: 2023-03-27 19:33
 **/
@Configuration
public class QRConfig {

    //采用JavaConfig的方式显示注入hutool中 生成二维码
    @Bean
    public QrConfig qrConfig() {
        //初始宽度和高度
        QrConfig qrConfig = new QrConfig(300, 300);
        //设置边距，即二维码和边框的距离
        qrConfig.setMargin(2);
        //设置前景色
        qrConfig.setForeColor(Color.BLACK.getRGB());
        //设置背景色
        qrConfig.setBackColor(Color.WHITE.getRGB());
        return qrConfig;

    }
}
