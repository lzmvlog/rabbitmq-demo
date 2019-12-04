package com.shaojie.rabbit.mq.demo.subscription;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.shaojie.rabbit.mq.demo.utils.ConnectionUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author ShaoJie
 * @Date 2019年11月27 16:50
 * @Description: subscription 订阅模式 - 发送信息
 */
public class Send {

    private static final String EXCHANGE_NAME = "test_exchange_fanout";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 获取连接
        Connection connection = ConnectionUtils.getConnection();
        // 使用内部分配的通道号创建一个新通道
        Channel channel = connection.createChannel();
        /**
         * exchange 交换机
         * type 交换机类型
         */
        /**
         * exchange 交换机 一方面 是接收生产者的信息 另一方面是想队列推送信息
         *          类型:
         *          fanout 不处理路由键  fanout exchange
         *          Direct 处理路由键   Direct exchange
         */
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        // 发送信息
        String message = "hello exchange";
        // 发布信息
        /**
         * exchange 将消息发布到的交换机
         * routingKey 路由键
         * props 消息的其他属性-路由标头等
         * body 发送的信息主体 byte[] 数组
         */
        channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
        System.out.println("-send message:" + message);

        channel.close();
        connection.close();
        // 消息发送出去 就丢失了 因为交换机没有存储消息的能力 在 rabitmq 里面 只有队列才有存储的能力
        // 此时还没有队列绑定到交换机 所以数据丢失
        // 即如果现在交换机已经启动 信息才能发送成功
    }
}
