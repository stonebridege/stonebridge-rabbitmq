package com.stonebridge.rabbitmq.seven;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import com.stonebridge.rabbitmq.utils.RabbitMqUtils;

import java.nio.charset.StandardCharsets;

/**
 * 声明主题交换机以及相关队列
 */
public class ReceiveLogsTopic01 {
    /**
     * 交换机名称
     *
     * @param args
     */
    public static final String EXCHANGE_NAME = "topic_logs";
    public static final String QUEUE_NAME = "Q1";
    public static final String ROUTING_KEY = "*.orange.*";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //1.声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        //2.声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null).getQueue();
        //3.设置ROUTING_KEY，绑定队列和交换机
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);
        //4.等待接受消息
        System.out.println("ReceiveLogsTopic01等待接受的消息，把接收的消息打印在屏幕上……");
        /**
         * 5.1.接收消息
         */
        DeliverCallback deliverCallback = (String consumerTag, Delivery message) -> {
            String dataString = new String(message.getBody(), StandardCharsets.UTF_8) + "，绑定键：" + message.getEnvelope().getRoutingKey();
            System.out.println("ReceiveLogsTopic01接收到的消息：" + dataString);
        };
        //5.接收消息
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, CancelCallback -> {
        });
    }
}
