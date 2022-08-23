package com.stonebridge.springbootrabbitmq.consumer;

import com.rabbitmq.client.Channel;
import com.stonebridge.springbootrabbitmq.config.ConfirmConfig;
import com.stonebridge.springbootrabbitmq.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Component
public class ConfirmConsumer {
    @RabbitListener(queues = ConfirmConfig.CONFIRM_QUEUE_NAME)
    public void receiveConfirmMsg(Message message, Channel channel) {
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        String routingKey = message.getMessageProperties().getReceivedRoutingKey();
        log.info("<消费者>收到当前时间:{},收到confirm.queue消息:{},其routingKey:{}", DateUtil.getDateStr(new Date()), msg, routingKey);
    }
}
