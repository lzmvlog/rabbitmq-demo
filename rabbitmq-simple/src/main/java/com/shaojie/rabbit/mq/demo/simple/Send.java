package com.shaojie.rabbit.mq.demo.simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.shaojie.rabbit.mq.demo.utils.ConnectionUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author ShaoJie
 * @Date 2019年11月27 16:50
 * @Description: 简单的对列 - 发送信息
 */
public class Send {

    private static final String QUEUE = "test_simple";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 获取连接
        Connection connection = ConnectionUtils.getConnection();
        // 使用内部分配的通道号创建一个新通道
        Channel channel = connection.createChannel();
        // 创建声明队列
        /**
         * durable 是否是持久化队列 已声明过的队列不允许更改
         * exclusive 是否是一个独占队列(仅限此连接)
         * autoDelete 是否自动删除队列(服务器将在不再使用它时将其删除)
         * arguments 队列的其他属性（构造参数）
         */
        // 队列名称  是否是持久化队列 是否是一个独占队列(仅限此连接) 是否自动删除队列(服务器将在不再使用它时将其删除) 队列的其他属性（构造参数）
        channel.queueDeclare(QUEUE, false, false, false, null);
        // 发送的信息
        String message = "hello rabbitmq!";
        // 发布信息
        /**
         * exchange 将消息发布到的交换机
         * routingKey 路由键
         * props 消息的其他属性-路由标头等
         * body 发送的信息主体 byte[] 数组
         */
        channel.basicPublish("", QUEUE, null, message.getBytes());
        System.out.println("-send message: " + message);
        // 关闭资源
        channel.close();
        connection.close();
    }
}
