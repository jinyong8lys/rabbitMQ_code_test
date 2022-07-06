package com.mixia.rabbitmq01.producer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @program: rabbitmq_test
 * @description
 * @author: jiny
 * @create: 2022-05-30 17:38
 **/
public class Producer {
    //ctrl+shift+u小写转大写
    private static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.114.10");
        factory.setUsername("mixia");
        factory.setPassword("mixia1234");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        /**
         * 创建队列
         * 1、队列名称
         * 2、队列消息是否进行持久化
         * 3、该队列是否只供一个消费者进行消费，true-是，false-否。如果要声明独占队列（仅限于此连接），则为true
         * 4、如果我们正在声明自动删除队列，则为true（服务器将在不再使用时将其删除）
         *
         */
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        String message = "hello world";

        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());

        System.out.println("消息发送完毕");
    }

}
