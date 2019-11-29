package top.shaojie.rabbit.mq.demo.routing;

import com.rabbitmq.client.*;
import top.shaojie.rabbit.mq.demo.utils.ConnectionUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author ShaoJie
 * @Date 2019年11月28 13:01
 * @Description: routing 路由模式 接受信息 【消费者2】
 */
// work queue 轮询分发 信息均分
public class Receive2 {

    private static final String QUEUE = "test_exchange_sms_direct";
    private static final String EXCHANGE_NAME = "test_exchange_direct";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 获取连接
        Connection connection = ConnectionUtils.getConnection();
        // 使用内部分配的通道号创建一个新通道
        final Channel channel = connection.createChannel();
        // 声明队列
        channel.queueDeclare(QUEUE, false, false, false, null);
        // 处理的信息数量
        channel.basicQos(1);
        /**
         * queue 队列名称
         * exchange 绑定中消息从中流过的交换的名称
         * routingKey 用于绑定的路由密钥
         */
        // 定义 routingKey
        String routingKey = "info";
        // 绑定交换机
        channel.queueBind(QUEUE, EXCHANGE_NAME, routingKey);
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
        // 监听队列 注意这个参数true,如果我们设置为false,我们就需要手动确认消息的消费,否则这一条消息无法在中间件之中清除掉.
        channel.basicConsume(QUEUE, autoAck, consumer);
    }
}
