package com.stonebridge.springbootrabbitmq.controller;

import com.stonebridge.springbootrabbitmq.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@Slf4j
@RestController
@RequestMapping("ttl")
public class SendMsgController {

    private RabbitTemplate rabbitTemplate;

    @Autowired
    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @GetMapping("/sendMsg/{message}")
    public void sendMsg(@PathVariable String message) {
        String dateStr = DateUtil.getDateStr(new Date());
        rabbitTemplate.convertAndSend("X", "XA", "消息来自ttl为10s队列：" + message + ";消费端发出时间为：" + dateStr);
        rabbitTemplate.convertAndSend("X", "XB", "消息来自ttl为40s队列：" + message + ";消费端发出时间为：" + dateStr);
        log.info("当前时间：{},发出的死信队列消息：{}", dateStr, message);
    }
}
