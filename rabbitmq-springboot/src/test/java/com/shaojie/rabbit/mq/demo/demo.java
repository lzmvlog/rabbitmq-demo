package com.shaojie.rabbit.mq.demo;

import com.shaojie.rabbit.mq.demo.configuration.RabbitmqConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author ShaoJie
 * @Date 2019年11月30 22:03
 * @Description:
 */
@SpringBootTest
public class demo {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(){
        String message = "hello rabbitmq-spring";
        // 直接调用 convertAndSend 方法发送信息
        /**
         * exchange 交换机
         * routingKey 路由键
         * object 发送内容
         */
        rabbitTemplate.convertAndSend(RabbitmqConfiguration.QUEUE,message);
    }

    @Test
    public void test(){
        send();
    }
}
