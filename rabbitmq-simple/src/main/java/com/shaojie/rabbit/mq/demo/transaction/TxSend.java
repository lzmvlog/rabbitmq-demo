package com.shaojie.rabbit.mq.demo.transaction;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.shaojie.rabbit.mq.demo.utils.ConnectionUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author ShaoJie
 * @Date 2019年11月29 13:55
 * @Description: RabbitMq 的消息确认机智 （事务 + confirm） 事务 TxSend  AMQP
 */
public class TxSend {

    /**
     * 在 rabbitmq 中 我们可以通过持久化数据 解决 rabbitmq 服务器异常问题 的数据丢失问题
     * 问题：
     * 生产者将消息发送出去之后 消息到达 rabbitmq 服务器 默认的情况是不知道的
     * <p>
     * 两种方式：
     * AMQP 实现了事务机制
     *      TxSelect 用于将当前的 channel 设置成 transaction
     *      TxCommit 用于提交事务
     *      TxRollback 用于回滚事务
     *  缺点：降低 rabbitmq 的吞吐量
     * <p>
     * Confirm 模式
     * 生产者端 confirm 模式 的实现原理：
     *      生产者将信道 设置成 confirm 模式 一旦信道进去 confirm 模式 所有在该信道上面发布的信息都会被指派一个唯一的 ID （从 1 开始）
     *      一旦信息被投递到所有的匹配队列之后 broker 就会发送一个确认的给生产者（包含小心的唯一ID） 这就使得生产者 知道消息已经正确到达
     *      目的地的队列了 如果信息 和队列是可持久化的 那么确认信息 会将信息写入到自盘之后发出 broker 回传给生产者的确认消息中
     *      deliver-tag 域包含了去人信息的序列号 此外 broker 也可以设置 basic.ack 的 multiple 域 表示到这个序列号之前的所有信息都已经得到了处理
     *  优点: 异步处理
     *  异步模式：
     *      channel 对象提供的 confirmListener() 回调方法只包含 deliver-tag （当前 channel 发出的信息序号） 我们需要为每一个 channel
     *      维护一个 unconfirm 的小心序号集合 每 publish 一条数据 集合元素加 1 每回调一次 handleAck 方法 unconfirm 集合删掉相应的一条
     *      （multiple = false） 或多条 （multiple = true） 记录 从程序运行的效率上看 这个 unconfirm 集合最好采用有序集合 SortSet 储存结构
     */

    private static final String QUEUE = "test_queue_tx";

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
        // 发送的信息
        String message = "hello transaction TxSelect";
        try {
            // 开启事务模式
            channel.txSelect();
            // 发送信息
            /**
             * exchange 将消息发布到的交换机
             * routingKey 路由键
             * props 消息的其他属性-路由标头等
             * body 发送的信息主体 byte[] 数组
             */
            channel.basicPublish("", QUEUE, null, message.getBytes());
            // 模拟出错的情况 查看 是否被回滚 可能会降低吞吐量
            int num = 1 / 0;
            // 提交
            channel.txCommit();
            System.out.println("-send message " + message);
        } catch (IOException e) {
            channel.txRollback();
            System.out.println("-send message TxRollback");
        } finally {
            channel.close();
            connection.close();
        }
    }
}
