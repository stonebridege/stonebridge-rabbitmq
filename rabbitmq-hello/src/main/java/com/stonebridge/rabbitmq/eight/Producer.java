package com.stonebridge.rabbitmq.eight;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.stonebridge.rabbitmq.utils.RabbitMqUtils;

import java.nio.charset.StandardCharsets;

/**
 * 死信队列 之生产者代码
 */
public class Producer {
    //普通交换机名称
    public static final String NORMAL_EXCHANGE = "normal_exchange";
    //普通队列routing_key
    public static final String NORMAL_ROUTING_KEY = "zhangsan";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //死信消息，设置TTL,单位是毫秒 10000ms
        AMQP.BasicProperties props = new AMQP.BasicProperties().builder().expiration("10000").build();
        for (int i = 0; i < 10; i++) {
            String message = "info" + i;
            channel.basicPublish(NORMAL_EXCHANGE, NORMAL_ROUTING_KEY, props, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("发送消息：" + message);
        }
    }
}
