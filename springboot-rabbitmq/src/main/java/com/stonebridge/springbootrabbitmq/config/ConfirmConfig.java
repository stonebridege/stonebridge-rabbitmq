package com.stonebridge.springbootrabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 发布确认
 */
@Component
public class ConfirmConfig {
    // 定义交换机名称
    public static final String CONFIRM_EXCHANGE_NAME = "confirm.exchange";
    // 定义队列名称
    public static final String CONFIRM_QUEUE_NAME = "confirm.queue";
    // ROUTING_KEY
    public static final String ROUTING_KEY = "key1";

    /**
     * 交换机声明
     *
     * @return 交换机对象
     */
    @Bean("confirmExchange")
    public DirectExchange confirmExchange() {
        return new DirectExchange(CONFIRM_EXCHANGE_NAME);
    }

    /**
     * 队列声明
     *
     * @return 队列对象
     */
    @Bean("confirmQueue")
    public Queue confirmQueue() {
        return QueueBuilder.durable(CONFIRM_QUEUE_NAME).build();
    }

    /**
     * 交换机和队列的绑定
     *
     * @param confirmQueue    ：队列
     * @param confirmExchange :交换机
     * @return ：绑定的结果
     */
    @Bean
    public Binding queueBindingExchange(@Qualifier("confirmQueue") Queue confirmQueue,
                                        @Qualifier("confirmExchange") DirectExchange confirmExchange) {
        return BindingBuilder.bind(confirmQueue).to(confirmExchange).with(ROUTING_KEY);
    }
}
