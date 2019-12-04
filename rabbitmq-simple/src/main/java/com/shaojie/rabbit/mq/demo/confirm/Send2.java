package com.shaojie.rabbit.mq.demo.confirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.shaojie.rabbit.mq.demo.utils.ConnectionUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author ShaoJie
 * @Date 2019年11月29 14:38
 * @Description: confirm 模式 批量信息发送
 */
public class Send2 {

    private static final String QUEUE = "test_queue_confirm2";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 获取连接
        Connection connection = ConnectionUtils.getConnection();
        // 创建发送渠道
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
        /**
         * 生产者 调用 confirmSelect() 将 channel 设置为 confirm
         * 注意 ：
         *  transaction 模式和 confirm 不能同时使用 否则可能会出错
         */
        channel.confirmSelect();
        // 发送的信息
        String message = "hello transaction TxSelect";
        for (int i = 0; i < 10; i++) {
            // 发送信息
            /**
             * exchange 将消息发布到的交换机
             * routingKey 路由键
             * props 消息的其他属性-路由标头等
             * body 发送的信息主体 byte[] 数组
             */
            channel.basicPublish("", QUEUE, null, message.getBytes());
        }
        try {
            if (!channel.waitForConfirms()) {
                System.out.println("-send message failed");
            } else {
                System.out.println("-send message:" + message);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        channel.close();
        connection.close();
    }
}
