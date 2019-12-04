package com.shaojie.rabbit.mq.demo.confirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.shaojie.rabbit.mq.demo.utils.ConnectionUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.TimeoutException;

/**
 * @author ShaoJie
 * @Date 2019年11月29 14:38
 * @Description: confirm 模式 批量信息发送
 */
public class Send3 {

    private static final String QUEUE = "test_queue_confirm3";

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
        // 未确认的信息标识
        final SortedSet<Long> confirmSet = Collections.synchronizedSortedSet(new TreeSet<Long>());
        // 通道添加监听
        channel.addConfirmListener(new ConfirmListener() {
            // 没有问题的 handleAck
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                if (multiple) {
                    System.out.println("----handleAck----multiple true");
                    confirmSet.headSet(deliveryTag + 1).clear();
                } else {
                    System.out.println("----handleAck----multiple false");
                    confirmSet.remove(deliveryTag);
                }
            }

            // 发送失败的
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                if (multiple) {
                    System.out.println("----handleNack----multiple true");
                    confirmSet.headSet(deliveryTag + 1).clear();
                } else {
                    System.out.println("----handleNack----multiple false");
                    confirmSet.remove(deliveryTag);
                }
            }
        });
        // 发送的信息
        String message = "hello transaction confirm";

        while (true) {
            long seqNo = channel.getNextPublishSeqNo();
            channel.basicPublish("", QUEUE, null, message.getBytes());
            confirmSet.add(seqNo);
        }

    }
}
