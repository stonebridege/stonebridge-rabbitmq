package com.stonebridge.rabbitmq.one;

import com.rabbitmq.client.*;

public class Consumer {
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        //1.创建一个链接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //2.设置ip地址
        //http://192.168.234.102:15672/#/exchanges
        factory.setHost("192.168.234.102");
        //3.设置用户名和密码
        factory.setUsername("admin");
        factory.setPassword("123");

        //4.创建链接(Connection)
        Connection connection = factory.newConnection();
        //5.创建信道(Connection)
        Channel channel = connection.createChannel();

        //6.推送的消息如何进行消费的接口回调
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody());
            System.out.println("接受的消息是：" + message);
        };

        //7.取消消费的一个回调接口 如在消费的时候队列被删除掉了
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("消息消费被中断");
        };
        /*
         * 8.消费者消费消息
         * 8.1.消费哪个队列
         * 8.2.消费成功之后是否自动应答
         *      -true：代表的自动应答
         *      -false：代表手动应答
         * 8.3.消费者未成功消费的回调
         * 8.4.消费者取消消费的的回调
         */
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }
}
