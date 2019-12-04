package com.shaojie.rabbit.mq.demo.confirm;

import com.rabbitmq.client.*;
import com.shaojie.rabbit.mq.demo.utils.ConnectionUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author ShaoJie
 * @Date 2019年11月29 14:42
 * @Description: confirm 模式 消费者
 */
public class Receive {

    private static final String QUEUE = "test_queue_confirm2";

    public static void main(String[] args) throws IOException, TimeoutException {
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
        /**
         * queue 队列名称
         * autoAck 服务器是否应该考虑*消息一旦送达即被确认；如果服务器应该期望*显式确认，则返回false
         * callback 消费者对象的接口
         */
        channel.basicConsume(QUEUE, true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                super.handleDelivery(consumerTag, envelope, properties, body);
                System.out.println("-receive message:" + new String(body));
            }
        });
    }
}
