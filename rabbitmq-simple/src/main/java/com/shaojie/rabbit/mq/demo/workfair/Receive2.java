package com.shaojie.rabbit.mq.demo.workfair;

import com.rabbitmq.client.*;
import com.shaojie.rabbit.mq.demo.utils.ConnectionUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author ShaoJie
 * @Date 2019年11月28 13:11
 * @Description: work queue 工作队列 接受信息 【消费者2】
 */
public class Receive2 {

    private static final String QUEUE = "test_work";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 获取连接
        Connection connection = ConnectionUtils.getConnection();
        // 使用内部分配的通道号创建一个新通道
        final Channel channel = connection.createChannel();
        // 声明队列
        channel.queueDeclare(QUEUE, false, false, false, null);

        // 处理的信息数数量
        channel.basicQos(1);

        // 定义消费者
        Consumer consumer = new DefaultConsumer(channel) {
            // 获取到达的消息
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                System.out.println("-receive[2] message:" + new String(body));
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        };
        // 关闭自动应答 autoAck 消息应答 默认是打开的 true
        boolean autoAck = false;
        channel.basicConsume(QUEUE, autoAck, consumer);
    }
}
