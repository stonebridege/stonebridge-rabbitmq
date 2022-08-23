package com.stonebridge.springbootrabbitmq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 回调接口，无论交换机是否接收到消息，
 */
@Component
@Slf4j
public class MyCallBack implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {

    private RabbitTemplate rabbitTemplate;

    @Autowired
    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * 因为RabbitTemplate.ConfirmCallback是RabbitTemplate的内部接口，因此要获得RabbitTemplate的实例，
     * 将该接口的实现类注入到RabbitTemplate.ConfirmCallback。
     * \@PostConstruct是Java自带的注解，在方法上加该注解会在项目启动的时候执行该方法，也可以理解为在spring容器初始化的时候执行该方法。 该类的执行过程：
     * 1.MyCallBack实例化
     * 2.执行@Autowired注入
     * 3.最后执行init()方法
     */
    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }

    /**
     * 交换机确认回调方法：交换机不管是否成功或失败收到消息都会触发的一个回调方法
     *
     * @param correlationData ：保存回调信息的id及相关信息，需要在生产者发送时封装添加
     * @param ack             ：交换机接收成功返回true；交换机接收失败返回false；
     * @param cause           ：失败的原因
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData != null ? correlationData.getId() : "";
        if (ack) {
            log.info("回调函数触发;交换机已经收到 id 为:{}的消息", id);
        } else {
            log.info("回调函数触发;交换机还未收到 id 为:{}消息,由于原因:{}", id, cause);
        }
    }

    //数可以在当消息传递过程中不可达目的地时将消息返回给生产者,只有不可达目的地的时候，才进行回退
    @Override
    public void returnedMessage(ReturnedMessage returned) {
        Message message = returned.getMessage();
        String msg = new String(message.getBody());
        String exchange = returned.getExchange();
        String replyText = returned.getReplyText();
        String routingKey = returned.getRoutingKey();
        log.error("回退的消息:{}, 被交换机:{} 退回，退回原因:{}, 路由key:{}", msg, exchange, replyText, routingKey);
    }
}
