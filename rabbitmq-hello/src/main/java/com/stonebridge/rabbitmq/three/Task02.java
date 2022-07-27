package com.stonebridge.rabbitmq.three;

import com.rabbitmq.client.Channel;
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
        //2.声明队列
        channel.queueDeclare(TASK_QUEUE_NAME, false, false, false, null);
        //3.从控制台中输入信息
        //从控制台输入信息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish("", TASK_QUEUE_NAME, null, message.getBytes("UTF-8"));
            System.out.println("发送消息完成:" + message);
        }
    }
}
