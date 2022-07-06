package com.mixia.rabbitmq.r03;

import com.mixia.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @program: rabbitmq_test
 * @description
 * @author: jiny
 * @create: 2022-06-02 17:12
 **/
public class Consumer0302 {
    public static final String QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();

        System.out.println("c2等待接收消息处理时间较短");

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            try {
                Thread.sleep(30000L);
            }catch (InterruptedException e){

            }
            System.out.println("接收到消息：" + new String(message.getBody()));

            //手动消息应答
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        };

        CancelCallback cancelCallback = consumerTag -> {

        };
        //设置不公平分发
        channel.basicQos(5);
        //手动应答
        channel.basicConsume(QUEUE_NAME, false, deliverCallback, cancelCallback);
    }

}
