package com.stonebridge.rabbitmq.eight;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import com.stonebridge.rabbitmq.utils.RabbitMqUtils;

import java.nio.charset.StandardCharsets;

public class Consumer02 {
    //普通队列名称
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        System.out.println("Consumer02等待接受的消息，把接收的消息打印在屏幕上……");
        //7.接收消息
        DeliverCallback deliverCallback = (String consumerTag, Delivery message) -> {
            System.out.println("Consumer02打印接收到的消息：" + new String(message.getBody(), StandardCharsets.UTF_8));
        };
        channel.basicConsume(DEAD_QUEUE, true, deliverCallback, CancelCallback -> {
        });
    }
}
