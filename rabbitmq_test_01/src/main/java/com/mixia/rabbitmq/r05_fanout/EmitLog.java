package com.mixia.rabbitmq.r05_fanout;

import com.mixia.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * 扇出(广播)模式-接收方1
 * 此模式下所有接收方都会受到消息，与routing key无关
 *
 * @program: rabbitmq_test
 * @description
 * @author: jiny
 * @create: 2022-06-16 14:13
 **/
public class EmitLog {

    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            channel.basicPublish(EXCHANGE_NAME, "2", null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("发出消息：" + message);
        }



    }

}
