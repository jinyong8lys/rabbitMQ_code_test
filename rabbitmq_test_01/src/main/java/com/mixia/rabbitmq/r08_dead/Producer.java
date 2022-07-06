package com.mixia.rabbitmq.r08_dead;

import com.mixia.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;

/**
 * @program: rabbitmq_test
 * @description
 * @author: jiny
 * @create: 2022-06-24 16:59
 **/
public class Producer {
    //普通交换机名称
    private static final String NORMAL_EXCHANGE = "normal_exchange";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        //设置消息的 TTL 时间
        //AMQP.BasicProperties properties = new AMQP.BasicProperties()
        //        .builder().expiration("10000").build();


        for (int i = 1; i < 11 ; i++) {
            String message = "info_" + i;
            channel.basicPublish(NORMAL_EXCHANGE, "zhangsan", null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("生产者发送消息:" + message);
        }


    }
}
