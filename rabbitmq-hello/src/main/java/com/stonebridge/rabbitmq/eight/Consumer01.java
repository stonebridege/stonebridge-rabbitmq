package com.stonebridge.rabbitmq.eight;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import com.stonebridge.rabbitmq.utils.RabbitMqUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Consumer01 {
    //普通交换机名称
    public static final String NORMAL_EXCHANGE = "normal_exchange";
    //普通队列名称
    public static final String NORMAL_QUEUE = "normal_queue";
    //普通队列routing_key
    public static final String NORMAL_ROUTING_KEY = "zhangsan";

    //死信交换机名称
    public static final String DEAD_EXCHANGE = "dead_exchange";
    //死信队列的名称
    public static final String DEAD_QUEUE = "dead_queue";
    //死信队列routing_key
    public static final String DEAD_ROUTING_KEY = "lisi";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        //1.声明普通交换机，类型为direct
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        //2.声明死信交换机，类型为direct
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

        //3.声明普通队列以及设置死信参数
        //3.1.设置死信参数
        Map<String, Object> arguments = new HashMap<>();
        //3.2.普通队列设置死信交换机；根据该设置即消息一旦成为死信，就转发到死信交换机。
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        //3.3.普通队列设置死信路由；死信交换机根据死信路由将死信转发到死信队列
        arguments.put("x-dead-letter-routing-key", DEAD_ROUTING_KEY);
        //3.4.设置消息过期时间；也可以在生产者发消息的时候设置，可以做到根据不同的消息设置不同的ttl(推荐)
//        arguments.put("x-message-ttl", 10000);
        //3.5.设置正常队列的长度限制
//        arguments.put("x-max-length", 6);
        //3.6.声明普通队列,加死信参数
        channel.queueDeclare(NORMAL_QUEUE, false, false, false, arguments);

        //4.声明死信队列
        channel.queueDeclare(DEAD_QUEUE, false, false, false, null);

        //5.绑定普通的交换机和普通队列
        channel.queueBind(NORMAL_QUEUE, NORMAL_EXCHANGE, NORMAL_ROUTING_KEY);
        //6.绑定死信的交换机和死信队列
        channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, DEAD_ROUTING_KEY);

        System.out.println("Consumer01等待接受的消息，把接收的消息打印在屏幕上……");
        //7.接收消息
        DeliverCallback deliverCallback = (String consumerTag, Delivery message) -> {
            System.out.println("Consumer01打印接收到的消息：" + new String(message.getBody(), StandardCharsets.UTF_8));
        };
        channel.basicConsume(NORMAL_QUEUE, true, deliverCallback, CancelCallback -> {
        });
    }
}
