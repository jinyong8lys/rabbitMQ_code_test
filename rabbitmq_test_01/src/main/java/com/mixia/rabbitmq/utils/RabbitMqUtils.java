package com.mixia.rabbitmq.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @program: rabbitmq_test
 * @description
 * @author: jiny
 * @create: 2022-06-02 14:15
 **/
public class RabbitMqUtils {

    public static Channel getChannel() throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.114.10");
        factory.setUsername("mixia");
        factory.setPassword("mixia1234");

        Connection connection = factory.newConnection();
        return connection.createChannel();
    }
}
