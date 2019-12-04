package com.shaojie.rabbit.mq.demo.utils;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author ShaoJie
 * @Date 2019年11月27 16:35
 * @Description: RabbitMQ 连接工具 获取连接
 */
public class ConnectionUtils {

    /**
     * 获取 MQ 连接
     *
     * @return
     */
    public static Connection getConnection() throws IOException, TimeoutException {
        // 定义连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 设置连接地址
        factory.setHost("127.0.0.1");
        // 设置连接端口
        factory.setPort(5672);
        //  连接
        factory.setVirtualHost("test");
        // 用户名
        factory.setUsername("ShaoJie");
        // 密码
        factory.setPassword("123456");
        return factory.newConnection();
    }
}
