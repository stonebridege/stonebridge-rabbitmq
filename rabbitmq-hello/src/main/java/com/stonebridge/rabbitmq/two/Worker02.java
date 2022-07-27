package com.stonebridge.rabbitmq.two;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.stonebridge.rabbitmq.utils.RabbitMqUtils;

public class Worker02 {
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        //消息的接受
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("接受的消息为：" + new String(message.getBody()));
        };
        //消息被取消时，执行下面的内容
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println(consumerTag + "消息者取消消费接口回调逻辑");
        };
        System.out.println("C2等待接受消息……");
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }
}
