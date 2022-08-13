package com.stonebridge.springbootrabbitmq.consumer;

import com.rabbitmq.client.Channel;
import com.stonebridge.springbootrabbitmq.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Component
public class DeadLetterConsumer {
    @RabbitListener(queues = "QD")
    public void receiveD(Message message, Channel channel) {
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        log.info("当前时间：{},收到死信队列消息：{}", DateUtil.getDateStr(new Date()), msg);
    }
}
