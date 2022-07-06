package com.mixia.rabbitmq.r09_ttl;

import com.mixia.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;

/**
 * @program: rabbitmq_test
 * @description
 * @author: jiny
 * @create: 2022-06-27 17:02
 **/
public class Producer {
    //交换机X
    public static final String X_EXCHANGE = "X";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();

        channel.exchangeDeclare(X_EXCHANGE, BuiltinExchangeType.DIRECT);

        //channel.basicPublish(X_EXCHANGE, "XA", null, "消息来自 ttl 为 10S 的队列".getBytes(StandardCharsets.UTF_8));
        //System.out.println("生产者发送消息:消息来自 ttl 为 10S 的队列");

        //channel.basicPublish(X_EXCHANGE, "XB", null, "消息来自 ttl 为 40S 的队列".getBytes(StandardCharsets.UTF_8));
        //System.out.println("生产者发送消息:消息来自 ttl 为 10S 的队列");

        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().expiration("20000").build();

        channel.basicPublish(X_EXCHANGE, "XC", properties, "消息来自 消息ttl 为 20S 的队列".getBytes(StandardCharsets.UTF_8));
        System.out.println("生产者发送消息:消息来自 消息ttl 为 20S 的队列");
        properties = new AMQP.BasicProperties().builder().expiration("2000").build();
        channel.basicPublish(X_EXCHANGE, "XC", properties, "消息来自 消息ttl 为 2S 的队列".getBytes(StandardCharsets.UTF_8));
        System.out.println("生产者发送消息:消息来自 消息ttl 为 2S 的队列");
    }
}
