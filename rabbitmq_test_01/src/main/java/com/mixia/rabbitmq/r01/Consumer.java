package com.mixia.rabbitmq01.producer;

import com.rabbitmq.client.*;

/**
 * 消费者
 * @program: rabbitmq_test
 * @description
 * @author: jiny
 * @create: 2022-06-01 16:36
 **/
public class Consumer {
    //队列名称
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.114.10");
        factory.setUsername("mixia");
        factory.setPassword("mixia1234");

        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();

        DeliverCallback deliverCallback = (consumerTag, message) ->{
            System.out.println("message=" + new String(message.getBody()));
            System.out.println("consumerTag=" + consumerTag);
        };

        CancelCallback cancelCallback = (String consumerTag) -> {
            System.out.println("消息消费失败：" + consumerTag);
        };

        /**
         * 消费消息
         * 1、队列名称
         * 2、true-自动应答，false-手动应答
         * 3、消息消费成功回调
         * 4、取消消息消费的回调
         *
         */
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);





    }



}
