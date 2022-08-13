package com.stonebridge.springbootrabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


@Configuration
public class TtlGeneralConfig {
    //1.声明普通队列名称QC
    public static final String QUEUE_C = "QC";
    //2.声明普通队列QC的路由值
    public static final String QUEUE_C_ROUTING_KEY = "XC";
    //3.声明死信交换机
    public static final String Y_DEAD_LETTER_EXCHANGE = "Y";
    //4.声明死信routingkey
    public static final String DEAD_LETTER_ROUTING_KEY = "YD";

    //声明队列queueC
    @Bean
    public Queue queueC() {
        Map<String, Object> arguments = new HashMap<>(3);
        //设置死信交换机
        arguments.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);
        //设置死信routingkey
        arguments.put("x-dead-letter-routing-key", DEAD_LETTER_ROUTING_KEY);
        return QueueBuilder.durable(QUEUE_C).withArguments(arguments).build();
    }

    //绑定queueC和X交换机
    @Bean
    public Binding queueCBindingY(@Qualifier("queueC") Queue queueC, @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueC).to(xExchange).with(QUEUE_C_ROUTING_KEY);
    }
}
