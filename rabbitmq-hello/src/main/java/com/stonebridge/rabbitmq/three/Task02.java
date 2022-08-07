package com.stonebridge.rabbitmq.three;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.stonebridge.rabbitmq.utils.RabbitMqUtils;

import java.util.Scanner;

/**
 * 消息在手动应答时不丢失，放回队列中重新消费
 */
public class Task02 {
    public static final String TASK_QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception {
        //1.获取信道
        Channel channel = RabbitMqUtils.getChannel();
        channel.confirmSelect();
        //2.声明队列
        boolean durable = true; //需要让Queue进行持久化
        channel.queueDeclare(TASK_QUEUE_NAME, durable, false, false, null);
        //3.从控制台中输入信息
        //从控制台输入信息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            //设置生产者发送消息为持久化消息(要求保存到磁盘上)，如果不设置则保存在内存上。
            channel.basicPublish("", TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));
            System.out.println("发送消息完成:" + message);
        }
    }
}
