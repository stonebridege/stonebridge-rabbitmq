package com.stonebridge.springbootrabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class DelayedQueueConfig {
    //1.声明交换机名称
    public static final String DELAYED_EXCHANGE_NAME = "delayed.exchange";
    //2.声明队列名称
    public static final String DELAYED_QUEUE_NAME = "delayed.queue";
    //3.声明交换机和队列之间的routingkey
    public static final String DELAYED_ROUTING_KEY = "delayed.routingKey";

    //4.自定义交换机;因为引入了插件，这里需要自定义的是一个延迟交换机
    @Bean
    public CustomExchange delayedExchange() {
        /**
         * 1.交换机的名称
         * 2.交换机的类型
         * 3.是否需要持久化
         * 4.是否需要自动删除
         * 5.其他参数
         */
        Map<String, Object> args = new HashMap<>();
        //自定义交换机的类型
        args.put("x-delayed-type", "direct");
        return new CustomExchange(DELAYED_EXCHANGE_NAME, "x-delayed-message", true, false, args);
    }

    //5.定义一个队列
    @Bean
    public Queue delayedQueue() {
        return new Queue(DELAYED_QUEUE_NAME);
    }

    //6.通过routing-key绑定延迟队列和延迟交换机
    @Bean
    public Binding delayedQueueBindingDelayExchange(@Qualifier("delayedQueue") Queue delayedQueue,
                                                    @Qualifier("delayedExchange") CustomExchange delayedExchange) {
        return BindingBuilder.bind(delayedQueue).to(delayedExchange).with(DELAYED_ROUTING_KEY).noargs();
    }
}
