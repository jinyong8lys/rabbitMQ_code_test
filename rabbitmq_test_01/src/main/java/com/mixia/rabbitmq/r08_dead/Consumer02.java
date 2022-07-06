package com.mixia.rabbitmq.r08_dead;

import com.mixia.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.util.HashMap;
import java.util.Map;

/**
 *  死信交换机-消费者2
 *
 * @program: rabbitmq_test
 * @description
 * @author: jiny
 * @create: 2022-06-24 15:50
 **/
public class Consumer02 {
    //死信交换机名称
    private static final String DEAD_EXCHANGE = "dead_exchange";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();

        //死信交换机
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

        //声明死信队列
        channel.queueDeclare("dead-queue", false, false, false, null);
        //绑定队列
        channel.queueBind("dead-queue", DEAD_EXCHANGE, "lisi");

        System.out.println("等待接收消息......");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("Consumer02 接收到消息" + message);
        };

        channel.basicConsume("dead-queue", deliverCallback, (consumerTag, sig) -> {});
    }



}
