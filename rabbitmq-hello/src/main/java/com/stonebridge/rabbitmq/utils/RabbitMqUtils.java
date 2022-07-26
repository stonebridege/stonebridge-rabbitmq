package com.stonebridge.rabbitmq.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 链接工厂(ConnectionFactory)创建信道(Channel)的工具类
 */
public class RabbitMqUtils {
    //得到一个连接的 channel
    public static Channel getChannel() throws Exception {
        //1.创建一个连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //2.设置ip地址
        factory.setHost("192.168.234.102");
        //3.设置用户名
        factory.setUsername("admin");
        //4.设置密码
        factory.setPassword("123");
        //5.生成链接
        Connection connection = factory.newConnection();
        //6.生成信道
        Channel channel = connection.createChannel();
        return channel;
    }
}
