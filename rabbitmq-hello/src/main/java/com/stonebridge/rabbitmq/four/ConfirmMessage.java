package com.stonebridge.rabbitmq.four;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.MessageProperties;
import com.stonebridge.rabbitmq.utils.RabbitMqUtils;

import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class ConfirmMessage {
    public static final int MESSAGE_COUNT = 1000;

    public static void main(String[] args) throws Exception {
        //1.单个确认;发布1000个单独确认消息,耗时636ms
        ConfirmMessage.publishMessageIndividually();
        //2.批量确认发布;发布1000个批量确认消息,耗时77ms
        ConfirmMessage.publishMessageBatch();
        //3.异步发布确认;发布1000异步发布确认,耗时73ms
        ConfirmMessage.publishMessageAsync();
    }

    public static void publishMessageIndividually() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
        //开启发布确认
        channel.confirmSelect();
        long begin = System.currentTimeMillis();
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            //服务端返回 false 或超时时间内未返回，生产者可以消息重发
            boolean flag = channel.waitForConfirms();
            if (flag) {
                System.out.println("消息发送成功");
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个单独确认消息,耗时" + (end - begin) + "ms");
    }

    public static void publishMessageBatch() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
        //1.开启发布确认
        channel.confirmSelect();
        //2.批量确认消息大小
        int batchSize = 100;
        //3.未确认消息个数
        int outstandingMessageCount = 0;
        long begin = System.currentTimeMillis();
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            outstandingMessageCount++;
            //4.当发送100条消息后，进行一次批量确认。
            if (i % batchSize == 0) {
                channel.waitForConfirms();
                outstandingMessageCount = 0;
            }
        }
        //5.为了确保还有剩余没有确认消息 再次确认
        if (outstandingMessageCount > 0) {
            channel.waitForConfirms();
        }
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个批量确认消息,耗时" + (end - begin) + "ms");

    }

    public static void publishMessageAsync() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
        //1.开启发布确认
        channel.confirmSelect();
        //2.开始时间
        long begin = System.currentTimeMillis();

        /*
         * 3.消息确认成功回调函数
         * deliveryTag:消息的标记
         * multiple：是否为批量确认
         */
        ConfirmCallback ackCallback = (long deliveryTag, boolean multiple) -> {
            System.out.println("已确认的消息标记：" + deliveryTag);
        };

        /*
         * 4.消息确认失败回调函数
         * deliveryTag:消息的标记
         * multiple：是否为批量确认
         */
        ConfirmCallback nackCallback = (long deliveryTag, boolean multiple) -> {

            System.out.println("未确认的消息标记：" + deliveryTag);
        };

        /*
         *  5.准备消息监听器(监听哪些消息确认成功，哪些消息确认失败)
         *  - 监听哪些消息成功了
         *  - 监听哪些消息失败了
         */
        channel.addConfirmListener(ackCallback, nackCallback);

        //6.批量发送消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = "消息" + i;
            //发送消息
            channel.basicPublish("", queueName, null, message.getBytes());
        }
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "异步发布确认,耗时" + (end - begin) + "ms");
    }

    public static void publishMessageAsync1() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
        channel.confirmSelect();
        /*
         * 1.创建ConcurrentSkipListMap容器，线程安全有序的哈希表，适用于高并发的情况下.
         *  1.1.可以将序号和发送的消息进行关联(key-value)
         *  1.2.根据序号可以轻松批量删除条目
         *  1.3.支持高并发(多线程)
         */
        ConcurrentSkipListMap<Long, Object> skipListMap = new ConcurrentSkipListMap<Long, Object>();

        /*
         * 2.消息确认，删除到已经确认的消息，剩下的就是未确认的消息。
         */
        ConfirmCallback ackCallback = (long deliveryTag, boolean multiple) -> {
            //2.1.发消息的时候可能是批量发送的，所有要根据multiple判断是不是批量的
            if (multiple) {
                //2.1.1.拿到被批量发送的所有消息
                ConcurrentNavigableMap<Long, Object> confirmed = skipListMap.headMap(deliveryTag);
                confirmed.clear();
            } else {
                //2.2.不是批量发送的消息直接确认
                skipListMap.remove(deliveryTag);
            }
            System.out.println("确认的消息标记：" + deliveryTag);
        };
        /*
         * 3.消息确认失败回调函数
         * deliveryTag:消息的标记
         * multiple：是否为批量确认
         */
        ConfirmCallback nackCallback = (long deliveryTag, boolean multiple) -> {
            String message = (String) skipListMap.get(deliveryTag);
            System.out.println("未确认的消息标记：" + deliveryTag + ";未确认的消息是：" + message);
        };

        channel.addConfirmListener(ackCallback, nackCallback);
        /*
         * 4.批量发送消息
         */
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = "消息" + i;
            //发送消息
            channel.basicPublish("", queueName, null, message.getBytes());
            /*
             * 5.将已发送的消息保存到ConcurrentSkipListMap中
             * channel.getNextPublishSeqNo()获取下一次发布消息的序列号，每发送一次消息就打印一下
             */
            skipListMap.put(channel.getNextPublishSeqNo(), message);
            System.out.println("发送的消息：" + message);
        }
    }
}
