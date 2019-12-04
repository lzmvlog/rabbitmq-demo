package com.shaojie.rabbit.mq.demo.simple;

import com.rabbitmq.client.*;
import com.shaojie.rabbit.mq.demo.utils.ConnectionUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author ShaoJie
 * @Date 2019年11月27 17:29
 * @Description: 简单的对列 - 接收信息
 */
public class Receive {

    private static final String QUEUE = "test_simple";

    public static void main(String[] args) throws IOException, TimeoutException {
        newApi();
    }

    /**
     * 获取队列中的的信息
     *
     * @throws IOException
     * @throws TimeoutException
     */
    public static void newApi() throws IOException, TimeoutException {
        // 获取连接
        Connection connection = ConnectionUtils.getConnection();
        // 使用内部分配的通道号创建一个新通道
        Channel channel = connection.createChannel();
        /**
         * queue 队列名称
         * durable 如果我们声明一个持久队列，则为true（该队列将在服务器重启后继续存在）
         * exclusive 如果我们声明一个独占队列，则为true（仅限此连接）
         * autoDelete 如果我们声明一个自动删除队列，则为true（服务器将在不再使用它时将其删除
         * arguments 队列的其他属性（构造参数）
         */
        // 声明队列
        channel.queueDeclare(QUEUE, false, false, false, null);
        // 创建一个消费者
        // 构造一个新实例并记录其与传入通道的关联。
        Consumer consumer = new DefaultConsumer(channel) {
            // 获取到达的消息
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                System.out.println("-receive message:" + new String(body));
            }
        };

        // 监听队列 注意这个参数true,如果我们设置为false,我们就需要手动确认消息的消费,否则这一条消息无法在中间件之中清除掉.
        /**
         * 监听队列 注意这个参数true 如果我们设置为false 我们就需要手动确认消息的消费 否则这一条消息无法在中间件之中清除掉
         * 当这个参数为 true 时 开启自动应答的模式 这时 rabbitmq 发送完信息 就会将信息 从从内存中删除
         * 此时杀时消费者 信息就会丢失
         * 当这个参数为 false 时 开启手动应答 这时如果有一个消费者挂掉 就会有另一个消费者来消费
         * 消费完成后 告诉 rabbitmq 信息处理完成 可以删除
         */
        channel.basicConsume(QUEUE, true, consumer);
    }
}
