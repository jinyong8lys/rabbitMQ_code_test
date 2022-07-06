package com.mixia.rabbitmq.r07_topic;

import com.mixia.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;

/**
 * @program: rabbitmq_test
 * @description
 * @author: jiny
 * @create: 2022-06-20 16:45
 **/
public class EmitLogTopic {
    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();

        //定义交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

        channel.basicPublish(EXCHANGE_NAME, "2.1.rabbit", null, "123".getBytes(StandardCharsets.UTF_8));



    }
}
