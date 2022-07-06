package com.test.r03_backup;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @program: rabbitmq_test
 * @description
 * @author: jiny
 * @create: 2022-07-01 11:29
 **/
@Slf4j
@Configuration
public class BackUpConfirmConfig {
    public static final String CONFIRM_EXCHANGE_NAME = "backup.confirm.exchange";
    public static final String CONFIRM_QUEUE_NAME = "backup.confirm.queue";
    public static final String BACKUP_EXCHANGE_NAME = "backup.backup.exchange";
    public static final String BACKUP_QUEUE_NAME = "backup.backup.queue";
    public static final String WARNING_QUEUE_NAME = "backup.warning.queue";

    //声明确认队列
    @Bean("confirmQueue")
    public Queue confirmQueue(){
        return QueueBuilder.durable(CONFIRM_QUEUE_NAME).build();
    }

    //声明确认交换机，并建立和备份交换机的绑定关系
    @Bean("confirmExchange")
    public DirectExchange confirmExchange(){
        ExchangeBuilder exchangeBuilder = ExchangeBuilder.directExchange(CONFIRM_EXCHANGE_NAME)
                .durable(true)
                //设置该交换机的备份交换机
                .withArgument("alternate-exchange", BACKUP_EXCHANGE_NAME);
        return (DirectExchange)exchangeBuilder.build();
    }

    //建立确认队列和确认交换机绑定关系
    @Bean
    public Binding queueBinding(@Qualifier("confirmQueue") Queue queue, @Qualifier("confirmExchange") DirectExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("key1");
    }

    //声明警告队列
    @Bean("warningQueue")
    public Queue warningQueue(){
        return QueueBuilder.durable(WARNING_QUEUE_NAME).build();
    }

    //声明备份队列
    @Bean("backQueue")
    public Queue backQueue(){
        return QueueBuilder.durable(BACKUP_QUEUE_NAME).build();
    }

    //声明备份交换机
    @Bean("backupExchange")
    public FanoutExchange backupExchange(){
        return new FanoutExchange(BACKUP_EXCHANGE_NAME);
    }

    //建立警告队列绑定关系
    @Bean
    public Binding warningBinding(@Qualifier("warningQueue") Queue queue, @Qualifier("backupExchange") FanoutExchange exchange){
        return BindingBuilder.bind(queue).to(exchange);
    }

    //建立备份队列绑定关系
    @Bean
    public Binding backupBinding(@Qualifier("backQueue") Queue queue, @Qualifier("backupExchange") FanoutExchange exchange){
        return BindingBuilder.bind(queue).to(exchange);
    }

}
