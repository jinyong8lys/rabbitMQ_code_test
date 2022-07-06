package com.mixia.rabbitmq.r09_ttl;

import com.mixia.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * 延迟队列-消费者2
 *
 * @program: rabbitmq_test
 * @description
 * @author: jiny
 * @create: 2022-06-27 16:41
 **/
public class Consumer02 {
    //死信交换机
    public static final String Y_DEAD_LETTER_EXCHANGE = "Y";
    //死信队列
    public static final String DEAD_LETTER_QUEUE = "QD";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        channel.exchangeDeclare(Y_DEAD_LETTER_EXCHANGE, BuiltinExchangeType.DIRECT);

        channel.queueDeclare(DEAD_LETTER_QUEUE, false, false, false, null);
        channel.queueBind(DEAD_LETTER_QUEUE, Y_DEAD_LETTER_EXCHANGE, "YD");

        System.out.println("Consumer02-YD等待接收消息......");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("Consumer02-YD接收到消息" + message);
        };

        channel.basicConsume(DEAD_LETTER_QUEUE, deliverCallback, (consumerTag, sig) -> {});

    }
}
