package com.mixia.rabbitmq.r10_delayedqueue;

import com.mixia.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: rabbitmq_test
 * @description
 * @author: jiny
 * @create: 2022-06-28 16:09
 **/
public class Producer {
    public static final String DELAYED_EXCHANGE_NAME = "delayed.exchange";
    public static final String DELAYED_ROUTING_KEY = "delayed.routingkey";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        //自定义交换机的类型
        Map<String, Object> map = new HashMap<>();
        map.put("x-delayed-type", "direct");
        channel.exchangeDeclare(DELAYED_EXCHANGE_NAME, "x-delayed-message", true, false, map);

        AMQP.BasicProperties properties = new AMQP.BasicProperties()
                .builder().expiration("20000").build();
        channel.basicPublish(DELAYED_EXCHANGE_NAME, DELAYED_ROUTING_KEY, properties, "消息来自 消息ttl 为 20S 的队列".getBytes(StandardCharsets.UTF_8));
        System.out.println(new Date() + "消息来自 消息ttl 为 20S 的队列");

        properties = new AMQP.BasicProperties()
                .builder().expiration("2000").build();
        channel.basicPublish(DELAYED_EXCHANGE_NAME, DELAYED_ROUTING_KEY, properties, "消息来自 消息ttl 为 2S 的队列".getBytes(StandardCharsets.UTF_8));
        System.out.println(new Date() + "消息来自 消息ttl 为 2S 的队列");
    }
}
