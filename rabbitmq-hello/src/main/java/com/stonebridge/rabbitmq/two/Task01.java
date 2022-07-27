package com.stonebridge.rabbitmq.two;

import com.rabbitmq.client.Channel;
import com.stonebridge.rabbitmq.utils.RabbitMqUtils;

import java.util.Scanner;

public class Task01 {
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        /**
         * 6.生成一个队列
         * 6.1.队列名称
         * 6.2.队列里面的消息是否持久化(磁盘保存)，默认消息存储在内存中
         * 6.3.该队列是否只供一个消费者进行消费，是否进行共享。 true：可以多个消费者消费，false：只能一个消费者消费
         * 6.4.是否自动删除，最后一个消费者端开连接以后，该队列是否自动删除；true自动删除，false不自动删除。
         * 6.5.其他参数
         */
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        //从控制台输入信息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println("发送消息完成:" + message);
        }
    }
}
