package com.mixia.rabbitmq.r10_delayedqueue;

import com.mixia.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 基于插件的延迟队列-消费者
 *
 * @program: rabbitmq_test
 * @description
 * @author: jiny
 * @create: 2022-06-28 14:57
 **/
public class DelayedQueueConsumer {
    public static final String DELAYED_QUEUE_NAME = "delayed.queue";
    public static final String DELAYED_EXCHANGE_NAME = "delayed.exchange";
    public static final String DELAYED_ROUTING_KEY = "delayed.routingkey";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();

        //自定义交换机的类型
        Map<String, Object> map = new HashMap<>();
        map.put("x-delayed-type", "direct");
        channel.exchangeDeclare(DELAYED_EXCHANGE_NAME, "x-delayed-message", true, false, map);

        channel.queueDeclare(DELAYED_QUEUE_NAME, false, false, false, null);
        channel.queueBind(DELAYED_QUEUE_NAME, DELAYED_EXCHANGE_NAME, DELAYED_ROUTING_KEY);

        System.out.println("DelayedQueueConsumer-delayed.routingkey等待接收消息......");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "utf-8");
            System.out.println(new Date() + "DelayedQueueConsumer-delayed.routingkey接收到消息：" + message);
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };

        channel.basicConsume(DELAYED_QUEUE_NAME, deliverCallback, (consumerTag, sig) -> {});

    }
}
