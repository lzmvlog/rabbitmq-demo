package com.shaojie.rabbit.mq.demo;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author ShaoJie
 * @Date 2019年12月02 15:34
 * @Description: Spring - rabbitmq Simple
 */
public class Send {

    public static void main(String[] args) throws InterruptedException {
        AbstractApplicationContext context = new ClassPathXmlApplicationContext("application.xml");
        RabbitTemplate template = context.getBean(RabbitTemplate.class);
        String message = "spring - rabbitmq";
        template.convertAndSend("hello " + message);
        System.out.println("-send message :" + message);
        // 销毁容器
        context.close();
    }

}
