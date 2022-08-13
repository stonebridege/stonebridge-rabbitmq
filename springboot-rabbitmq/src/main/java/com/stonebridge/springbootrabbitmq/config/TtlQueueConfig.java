package com.stonebridge.springbootrabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 延迟队列测试，声明需要各种组件，配置文件类代码。
 */
@Configuration
public class TtlQueueConfig {
    //1.定义常量
    //1.1-1普通交换机的名称
    public static final String X_EXCHANGE = "X";
    //1.1-2.死信交换机名称
    public static final String Y_DEAD_LETTER_EXCHANGE = "Y";
    //1.2-1普通队列名称QA
    public static final String QUEUE_A = "QA";
    //1.2-2普通队列名称QB
    public static final String QUEUE_B = "QB";
    //1.2-3死信队列名称QD
    public static final String DEAD_LETTER_QUEUE = "QD";
    //1.3-1死信交换机和死信队列间的routing-key
    public static final String DEAD_LETTER_ROUTING_KEY = "YD";
    //1.3-2 QueueA和死信交换机间的routing-key
    public static final String QUEUE_A_ROUTING_KEY = "XA";
    //1.3-3 QueueB和死信交换机间的routing-key
    public static final String QUEUE_B_ROUTING_KEY = "XB";
    //1.4.1 QueueA延迟时间10s
    public static final Integer TTL_10 = 10000;
    //1.4.2 QueueB延迟时间40s
    public static final Integer TTL_40 = 40000;

    //2.1.声明普通交换机x,xExchange是别名，注入时使用
    @Bean("xExchange")
    public DirectExchange xExchange() {
        return new DirectExchange(X_EXCHANGE);
    }

    //2.2.声明死信交换机
    @Bean("yExchange")
    public DirectExchange yExchange() {
        return new DirectExchange(Y_DEAD_LETTER_EXCHANGE);
    }

    //3.1.声明队列A,ttl为10s
    @Bean("queueA")
    public Queue queueA() {
        Map<String, Object> arguments = new HashMap<>(3);
        //声明当前队列绑定的死信交换机
        arguments.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);
        //声明当前队列的死信路由RoutingKey
        arguments.put("x-dead-letter-routing-key", DEAD_LETTER_ROUTING_KEY);
        //声明队列的TTL
        arguments.put("x-message-ttl", TTL_10);
        return QueueBuilder.durable(QUEUE_A).withArguments(arguments).build();
    }

    //3.2.声明队列,ttl为40s
    @Bean("queueB")
    public Queue queueB() {
        Map<String, Object> arguments = new HashMap<>(3);
        //声明当前队列绑定的死信交换机
        arguments.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);
        //声明当前队列的死信路由RoutingKey
        arguments.put("x-dead-letter-routing-key", DEAD_LETTER_ROUTING_KEY);
        //声明队列的TTL
        arguments.put("x-message-ttl", TTL_40);
        return QueueBuilder.durable(QUEUE_B).withArguments(arguments).build();
    }

    //3.3.声明死信队列
    @Bean("queueD")
    public Queue queueD() {
        return QueueBuilder.durable(DEAD_LETTER_QUEUE).build();
    }

    //4.1.绑定queueA和普通交换机
    @Bean
    public Binding queueABindingX(@Qualifier("queueA") Queue queueA, @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueA).to(xExchange).with(QUEUE_A_ROUTING_KEY);
    }

    //4.2.绑定queueB和普通交换机
    @Bean
    public Binding queueBBindingX(@Qualifier("queueB") Queue queueB, @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueB).to(xExchange).with(QUEUE_B_ROUTING_KEY);
    }

    //4.3.绑定死信队列和死信交换机
    @Bean
    public Binding queueDBindingY(@Qualifier("queueD") Queue queueD, @Qualifier("yExchange") DirectExchange yExchange) {
        return BindingBuilder.bind(queueD).to(yExchange).with(DEAD_LETTER_ROUTING_KEY);
    }
}
