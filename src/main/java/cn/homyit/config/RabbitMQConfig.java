package cn.homyit.config;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static cn.homyit.constant.MQPrefixConstants.*;

/**
 * @program: graduate-website
 * @description: rabbitMq配置
 * @author: Charon
 * @create: 2023-04-05 21:04
 **/
@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue emailQueue() {
        return new Queue(TEMPLATE_MESSAGE_QUEUE, true);
    }

    @Bean
    public FanoutExchange emailExchange() {
        return new FanoutExchange(TEMPLATE_MESSAGE_EXCHANGE, true, false);
    }


}
