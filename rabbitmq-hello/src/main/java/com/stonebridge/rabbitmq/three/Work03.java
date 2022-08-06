package com.stonebridge.rabbitmq.three;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.stonebridge.rabbitmq.utils.RabbitMqUtils;
import com.stonebridge.rabbitmq.utils.SleepUtils;

import java.nio.charset.StandardCharsets;


public class Work03 {
    public static final String TASK_QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        System.out.println("C1等待接受消息处理时间较短");

        /**
         * 接收消息
         */
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            SleepUtils.sleep(1);
            System.out.println("接受的消息类型：" + new String(message.getBody(), StandardCharsets.UTF_8));
            /**
             * 手动应答
             * 1.deliveryTag：消息的标记tag（消息的唯一标识）
             * 2.multiple：是否批量应答
             *   - false：代表只应答接收到的那个传递的消息，不批量应答信道中的消息
             *   - true：批量应答所有消息包括传递过来的消息
             */
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        };

        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("消息消费被中断");
        };
        int prefetchSize = 1;
        //设置不公平分发
        channel.basicQos(prefetchSize);
        //采用手动应答
        boolean autoAck = false;
        channel.basicConsume(TASK_QUEUE_NAME, autoAck, deliverCallback, cancelCallback);
    }
}
