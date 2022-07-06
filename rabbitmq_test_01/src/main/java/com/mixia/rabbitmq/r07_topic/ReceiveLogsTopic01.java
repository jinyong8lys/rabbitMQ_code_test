package com.mixia.rabbitmq.r07_topic;

import com.mixia.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

/**
 * 主题模式
 *
 * @program: rabbitmq_test
 * @description
 * @author: jiny
 * @create: 2022-06-20 14:46
 **/
public class ReceiveLogsTopic01 {
    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        //定义交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        //定义临时队列
        String queueName = channel.queueDeclare().getQueue();
        //绑定队列、交换机和路由规则
        channel.queueBind(queueName, EXCHANGE_NAME, "*.orange.*");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [01] Received '" +
                    delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
        };

        channel.basicConsume(queueName, deliverCallback, (consumerTag, sig) -> {});
    }



}
