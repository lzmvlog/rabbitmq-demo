package com.shaojie.rabbit.mq.demo.work;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.shaojie.rabbit.mq.demo.utils.ConnectionUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author ShaoJie
 * @Date 2019年11月27 19:51
 * @Description: work queue 工作队列 发送信息
 */
public class Send {

    private static final String QUEUE = "test_work";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 获取连接
        Connection connection = ConnectionUtils.getConnection();
        // 使用内部分配的通道号创建一个新通道
        Channel channel = connection.createChannel();
        // 声明队列
        channel.queueDeclare(QUEUE, false, false, false, null);
        // 循环发送信息
        for (int i = 0; i < 10; i++) {
            String message = "hello : " + i;
            // 发送信息
            /**
             * exchange 将消息发布到的交换机
             * routingKey 路由键
             * props 消息的其他属性-路由标头等
             * body 发送的信息主体 byte[] 数组
             */
            channel.basicPublish("", QUEUE, null, message.getBytes());
            System.out.println("-send message: " + message);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        channel.close();
        connection.close();

    }

}
