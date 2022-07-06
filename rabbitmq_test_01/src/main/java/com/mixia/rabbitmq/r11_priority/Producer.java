package com.mixia.rabbitmq.r11_priority;

import com.mixia.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;


/**
 * 优先级队列-生产者
 *
 * @program: rabbitmq_test
 * @description
 * @author: jiny
 * @create: 2022-07-01 15:57
 **/
public class Producer {
    private static final String QUEUE_NAME="hello";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        //给消息赋予一个 priority 属性
        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().priority(5).build();
        for (int i = 1; i <11; i++){
            String message  = "info_" + i;
            if(i == 5){
                channel.basicPublish("", QUEUE_NAME, properties, message.getBytes());
            }else{
                channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            }
            System.out.println("发送消息完成:" + message);
        }
    }
}
