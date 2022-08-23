package com.stonebridge.springbootrabbitmq.controller;

import com.stonebridge.springbootrabbitmq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/confirm")
public class ProducerController {
    private RabbitTemplate rabbitTemplate;

    @Autowired
    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * 开始发消息，测试确认
     *
     * @param message ：要发送的消息
     */
    @GetMapping("/sendConfirmMsg/{message}")
    public void sendConfirmMsg(@PathVariable("message") String message) {
        //封装CorrelationData信息，触发回调函数时获取数据
        CorrelationData correlationData = new CorrelationData("1111111111111");
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME, ConfirmConfig.ROUTING_KEY, message, correlationData);
        log.info("发送消息内容为:{}", message + ";其routing-key:" + ConfirmConfig.ROUTING_KEY);

        CorrelationData correlationData1 = new CorrelationData("2222222222222");
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME, ConfirmConfig.ROUTING_KEY + "999", message, correlationData1);
        log.info("发送消息内容为:{}", message + ";其routing-key:" + ConfirmConfig.ROUTING_KEY + "999");
    }
}
