package com.stonebridge.rabbitmq.five;

import com.rabbitmq.client.*;
import com.stonebridge.rabbitmq.utils.RabbitMqUtils;

/**
 * 消息的接受
 */
public class ReceiveLogs01 {
    /**
     * 定义交换机名称
     */
    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception {
        //1.获取信道
        Channel channel = RabbitMqUtils.getChannel();
        /**
         *  2.声明一个交换机
         *  exchange:交换机名称
         *  type:交换机类型
         */
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
        /**
         * 3.声明一个临时队列
         * 生成一个临时的队列，队列名称是随机的，当消费者断开与队列的链接，队列就删除了
         */
        String queueName = channel.queueDeclare().getQueue();
        //4.绑定交换机与队列
        channel.queueBind(queueName, EXCHANGE_NAME, "");

        //等待接受消息
        System.out.println("ReceiveLogs01等待接受的消息，把接收的消息打印在屏幕上……");

        /**
         * 5.1.接收消息
         */
        DeliverCallback deliverCallback = (String consumerTag, Delivery message) -> {
            System.out.println("ReceiveLogs01控制台打印接收到的消息：" + new String(message.getBody(), "UTF-8"));
        };
        //5.接收消息
        channel.basicConsume(queueName, true, deliverCallback, CancelCallback -> {
        });
    }
}
