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
public class Consumer0301 {
    public static final String QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();

        System.out.println("c1等待接收消息处理时间较短");

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            try {
                Thread.sleep(15000L);
            }catch (InterruptedException e){

            }
            System.out.println("接收到消息：" + new String(message.getBody()));

            //手动消息应答
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
            //channel.basicNack(message.getEnvelope().getDeliveryTag(), false, false);
        };

        CancelCallback cancelCallback = consumerTag -> {

        };
        //设置不公平分发,原理是通过设置信道堆积消息数来实现，堆积消息数达到设置值就不再往当前信道设置消息,
        //实际还是轮询原理
        // 所有消费者都要设置，否则不生效
        channel.basicQos(2);

        //手动应答
        channel.basicConsume(QUEUE_NAME, false, deliverCallback, cancelCallback);
    }

}
