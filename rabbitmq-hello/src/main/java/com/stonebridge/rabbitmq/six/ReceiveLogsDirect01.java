package com.stonebridge.rabbitmq.six;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import com.stonebridge.rabbitmq.utils.RabbitMqUtils;

public class ReceiveLogsDirect01 {
    public static final String EXCHANGE_NAME = "direct_logs";
    public static final String QUEUE_NAME = "console";
    public static final String ROUTING_KEY_1 = "info";
    public static final String ROUTING_KEY_2 = "warning";

    public static void main(String[] args) throws Exception {
        //1.获取信道
        Channel channel = RabbitMqUtils.getChannel();
        /*
         *  2.声明一个交换机
         *  exchange:交换机名称
         *  type:交换机类型
         */
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        /*
         * 3.声明一个临时队列
         * 生成一个临时的队列，队列名称是随机的，当消费者断开与队列的链接，队列就删除了
         */
        channel.queueDeclare(QUEUE_NAME, false, false, false, null).getQueue();

        //4.绑定交换机与队列,并设置routingKey，可以绑定多个
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY_1);
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY_2);

        //等待接受消息
        System.out.println("ReceiveLogsDirect01等待接受的消息，把接收的消息打印在屏幕上……");
        /*
         * 5.1.接收消息
         */
        DeliverCallback deliverCallback = (String consumerTag, Delivery message) -> {
            System.out.println("ReceiveLogsDirect01控制台打印接收到的消息：" + new String(message.getBody(), "UTF-8"));
        };
        //5.接收消息
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, CancelCallback -> {
        });
    }
}
