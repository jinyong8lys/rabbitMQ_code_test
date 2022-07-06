package com.mixia.rabbitmq.r03;

import com.mixia.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.util.Scanner;

/**
 * 消息手动应答代码
 *
 * @program: rabbitmq_test
 * @description
 * @author: jiny
 * @create: 2022-06-02 16:53
 **/
public class Producer0301 {
    public static final String QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        //定义队列
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入信息：");
        while (scanner.hasNext()){
            String message = scanner.nextLine();
            //发布消息
            //MessageProperties.PERSISTENT_TEXT_PLAIN用于将消息持久化到磁盘，不需要持久化传null
            channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));
        }
    }

}
