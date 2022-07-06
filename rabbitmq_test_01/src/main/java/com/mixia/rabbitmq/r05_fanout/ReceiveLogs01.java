package com.mixia.rabbitmq.r05_fanout;

import com.mixia.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;


/**
 * 扇出(广播)模式-接收方1
 * 此模式下所有接收方都会受到消息，与routing key无关
 *
 *
 * @program: rabbitmq_test
 * @description
 * @author: jiny
 * @create: 2022-06-14 16:45
 **/
public class ReceiveLogs01 {

    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        //声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT.getType());
        //声明队列-临时队列，消费者断开连接，队列自动删除
        String queueName = channel.queueDeclare().getQueue();
        //绑定队列和交换机
        channel.queueBind(queueName, EXCHANGE_NAME, "1");
        System.out.println("01等待接收消息");

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("01接收到消息：" + new String(message.getBody(), "utf-8"));
        };

        channel.basicConsume(queueName, true, deliverCallback, (consumerTag, sig) -> {});
    }



}
