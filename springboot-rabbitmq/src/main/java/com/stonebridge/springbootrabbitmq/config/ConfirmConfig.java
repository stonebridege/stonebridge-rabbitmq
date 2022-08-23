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
    //1.定义各种组件名称
    // 1.1.定义确认交换机名称
    public static final String CONFIRM_EXCHANGE_NAME = "confirm.exchange";
    // 1.2.定义确认队列名称
    public static final String CONFIRM_QUEUE_NAME = "confirm.queue";
    // 1.3.定义ROUTING_KEY
    public static final String ROUTING_KEY = "key1";
    //1.4.定义备份交换机名称
    public static final String BACKUP_EXCHANGE_NAME = "backup.exchange";
    //1.5.定义备份队列名称
    public static final String BACKUP_QUEUE_NAME = "backup.queue";
    //1.6.定义warning队列名称
    public static final String WARNING_QUEUE_NAME = "warning.queue";

    //2.声明交换机

    /**
     * 2.1.创建<确认交换机>对象
     * @return 确认交换机
     */
    @Bean("confirmExchange")
    public DirectExchange confirmExchange() {
        return ExchangeBuilder
                .directExchange(CONFIRM_EXCHANGE_NAME)
                .durable(true)
                .withArgument("alternate-exchange", BACKUP_EXCHANGE_NAME)
                .build();
    }

    /**
     * 2.2.创建<备份交换机>对象
     * @return 备份交换机
     */
    @Bean("backupExchange")
    public FanoutExchange backupExchange() {
        return new FanoutExchange(BACKUP_EXCHANGE_NAME);
    }

    //3.创建队列
    /**
     * 3.1.创建<确认队列>对象
     *
     * @return 确认队列
     */
    @Bean("confirmQueue")
    public Queue confirmQueue() {
        return QueueBuilder.durable(CONFIRM_QUEUE_NAME).build();
    }

    /**
     * 3.2.创建<备份队列>对象
     * @return 备份队列
     */
    @Bean("backupQueue")
    public Queue backupQueue() {
        return QueueBuilder.durable(BACKUP_QUEUE_NAME).build();
    }

    /**
     * 3.3.创建<警告队列>对象
     * @return 备份队列
     */
    @Bean("warningQueue")
    public Queue warningQueue() {
        return QueueBuilder.durable(WARNING_QUEUE_NAME).build();
    }

    //4.交换机和队列的绑定
    /**
     * 4.1.《确认交换机》和《确认队列》的绑定
     * @param confirmQueue    ：确认队列
     * @param confirmExchange ：确认交换机
     * @return ：绑定的结果
     */
    @Bean
    public Binding queueBindingExchange(@Qualifier("confirmQueue") Queue confirmQueue,
                                        @Qualifier("confirmExchange") DirectExchange confirmExchange) {
        return BindingBuilder.bind(confirmQueue).to(confirmExchange).with(ROUTING_KEY);
    }

    /**
     * 4.2.《备份交换机》和《备份队列》的绑定
     * @param backupQueue    ：备份队列
     * @param backupExchange ：备份交换机
     * @return ：绑定的结果
     */
    @Bean
    public Binding backupQueueBindingBackupExchange(@Qualifier("backupQueue") Queue backupQueue,
                                                    @Qualifier("backupExchange") FanoutExchange backupExchange) {
        return BindingBuilder.bind(backupQueue).to(backupExchange);
    }

    /**
     * 4.3.《备份交换机》和《警告队列》的绑定
     * @param warningQueue   ：警告队列
     * @param backupExchange ：备份交换机
     * @return ：绑定的结果
     */
    @Bean
    public Binding warningQueueBindingBackupExchange(@Qualifier("warningQueue") Queue warningQueue,
                                                     @Qualifier("backupExchange") FanoutExchange backupExchange) {
        return BindingBuilder.bind(warningQueue).to(backupExchange);
    }
}
