package com.shaojie.rabbit.mq.demo.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author ShaoJie
 * @Date 2019年11月30 20:59
 * @Description:
 */
@Configuration
public class RabbitmqConfiguration {

    public static final String QUEUE = "test_simple";
    public static final String EXCHANGE_NAME = "test_exchange_topic";

    // 声明交换机
//    @Bean(EXCHANGE_NAME)
//    public ExchangeBuilder exchange() {
//        // durable 持久化  mq重启之后还在
//        return ExchangeBuilder.topicExchange(EXCHANGE_NAME).durable(true).build();
//    }

    // 声明队列
//    @Bean(value = QUEUE)
    @Bean
    public Queue queue() {
        return new Queue(QUEUE);
    }

//    // 绑定交换机和队列
//    public Binding binding(@Qualifier(value = QUEUE) Queue queue,
//                           @Qualifier(value = EXCHANGE_NAME) Exchange exchange) {
//        return BindingBuilder.bind(queue).to(exchange).with("").noargs();
//    }


}
