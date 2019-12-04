package com.shaojie.rabbit.mq.demo.work;

import com.rabbitmq.client.*;
import com.shaojie.rabbit.mq.demo.utils.ConnectionUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author ShaoJie
 * @Date 2019年11月28 13:01
 * @Description: work queue 工作队列 接受信息 【消费者1】
 */
// work queue 轮询分发 信息均分
public class Receive1 {

    private static final String QUEUE = "test_work";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 获取连接
        Connection connection = ConnectionUtils.getConnection();
        // 使用内部分配的通道号创建一个新通道
        Channel channel = connection.createChannel();
        // 声明队列
        channel.queueDeclare(QUEUE, false, false, false, null);
        // 定义消费者
        Consumer consumer = new DefaultConsumer(channel) {
            // 获取到达的消息
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                System.out.println("-receive[1] message:" + new String(body));
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        // 监听队列 注意这个参数true,如果我们设置为false,我们就需要手动确认消息的消费,否则这一条消息无法在中间件之中清除掉.
        channel.basicConsume(QUEUE, true, consumer);
    }
}
