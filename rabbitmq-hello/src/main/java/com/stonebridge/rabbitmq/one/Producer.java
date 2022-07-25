package com.stonebridge.rabbitmq.one;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;


public class Producer {
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
        //当生产者与rabbitMQ直接创建了链接(Connection)，链接(Connection)并不是发消息的，一个链接(Connection)里可以产生多个信道(Channel)，信道(Channel)是专门法信息，因此要通过链接(Connection)获取信道(Channel)。
        //5.创建信道(Connection)
        Channel channel = connection.createChannel();
        /**
         * 6.生成一个队列
         * 6.1.队列名称
         * 6.2.队列里面的消息是否持久化(磁盘保存)，默认消息存储在内存中
         * 6.3.该队列是否只供一个消费者进行消费，是否进行共享。 true：可以多个消费者消费，false：只能一个消费者消费
         * 6.4.是否自动删除，最后一个消费者端开连接以后，该队列是否自动删除；true自动删除，false不自动删除。
         * 6.5.其他参数
         */
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        String message = "hello world";
        /**
         * 7.发送一个消息
         * 7.1.发送到哪个交换机
         * 7.2.路由的key是哪个，本次是队列的名称
         * 7.3.其他的参数信息
         * 7.4.发送消息的消息体
         */
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        System.out.println("消息发送完毕");
    }
}
