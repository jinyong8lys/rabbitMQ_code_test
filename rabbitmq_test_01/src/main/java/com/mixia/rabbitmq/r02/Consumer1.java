package com.mixia.rabbitmq.r02;

import com.mixia.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @program: rabbitmq_test
 * @description
 * @author: jiny
 * @create: 2022-06-02 14:34
 **/
public class Consumer1 {

    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("接收回调消息：" + new String(message.getBody()));
        };

        CancelCallback cancelCallback = consumerTag -> {
            System.out.println("接收取消回调消息");
        };

        System.out.println("c1接收回调消息...");
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }

}
