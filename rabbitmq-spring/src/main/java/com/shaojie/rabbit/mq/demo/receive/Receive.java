package com.shaojie.rabbit.mq.demo.receive;

import com.shaojie.rabbit.mq.demo.configuration.RabbitmqConfiguration;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author ShaoJie
 * @Date 2019年11月30 22:13
 * @Description:
 */
@Component
public class Receive {

    @RabbitListener(queues = {RabbitmqConfiguration.QUEUE})
    public void listener(String message){
        System.out.println("-message:" + message);
    }
}
