package com.stonebridge.springbootrabbitmq.consumer;

import com.rabbitmq.client.Channel;
import com.stonebridge.springbootrabbitmq.config.DelayedQueueConfig;
import com.stonebridge.springbootrabbitmq.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

//消费基于插件的延迟消息
@Slf4j
@Component
public class DelayQueueConsumer {
    @RabbitListener(queues = DelayedQueueConfig.DELAYED_QUEUE_NAME)
    public void receiveDelay(Message message, Channel channel) {
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        log.info("当前时间:{},收到插件延迟队列消息:{}", DateUtil.getDateStr(new Date()), msg);
    }
}
