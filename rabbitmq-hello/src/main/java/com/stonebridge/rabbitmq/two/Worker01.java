package com.stonebridge.rabbitmq.two;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.stonebridge.rabbitmq.utils.RabbitMqUtils;

/**
 * 工作线程(消费者)
 */
public class Worker01 {
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        //1.获取信道(Channel)
        Channel channel = RabbitMqUtils.getChannel();

        //2.消息的接受
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("接受的消息为：" + new String(message.getBody()));
        };
        //3.消息被取消时，执行下面的内容
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println(consumerTag + "消息者取消消费接口回调逻辑");
        };
        System.out.println("C1等待接受消息……");
        /*
         * 4.消费者消费消息
         * 4.1.消费哪个队列
         * 4.2.消费成功之后是否自动应答
         *      -true：代表的自动应答
         *      -false：代表手动应答
         * 4.3.消费者未成功消费的回调
         * 4.4.消费者取消消费的的回调
         */
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }
}
