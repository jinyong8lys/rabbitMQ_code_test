package com.mixia.rabbitmq.r04;

import com.mixia.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;

import java.util.UUID;

/**
 * @program: rabbitmq_test
 * @description
 * @author: jiny
 * @create: 2022-06-07 15:43
 *
 *  发布确认模式
 *      1.单个确认
 *      2、批量确认
 *      3、异步确认
 *
 **/
public class ConfirmMessage {

    public static void main(String[] args) throws Exception{
        //1、单个确认,时间：932
        //ConfirmMessage.publishMessageIndividually();

        //2、批量确认,时间：94
        //ConfirmMessage.publishMessageBatch();

        //3、异步确认,时间：31
        ConfirmMessage.publishMessageAsync();
    }

    private static void publishMessageIndividually() throws Exception{
        Channel channel = RabbitMqUtils.getChannel();

        String queneName = UUID.randomUUID().toString();
        channel.queueDeclare(queneName, true, false, false, null);
        //开启发布确认
        channel.confirmSelect();
        //开始时间
        long start = System.currentTimeMillis();

        for (int i = 0; i < 1000; i++){
            channel.basicPublish("", queneName, null, (i+"").getBytes());
            boolean flag = channel.waitForConfirms();
            if(flag){
                System.out.println("消息发送成功");
            }
        }
        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("时间：" + (end - start));
    }

    private static void publishMessageBatch() throws Exception{
        Channel channel = RabbitMqUtils.getChannel();

        String queneName = UUID.randomUUID().toString();
        channel.queueDeclare(queneName, true, false, false, null);
        //开启发布确认
        channel.confirmSelect();
        //开始时间
        long start = System.currentTimeMillis();

        for (int i = 1; i <= 1000; i++){
            channel.basicPublish("", queneName, null, (i+"").getBytes());

            if(i%100 == 0){
                channel.waitForConfirms();
            }
        }

        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("时间：" + (end - start));
    }

    private static void publishMessageAsync() throws Exception{
        Channel channel = RabbitMqUtils.getChannel();

        String queneName = UUID.randomUUID().toString();
        channel.queueDeclare(queneName, true, false, false, null);
        //开启发布确认
        channel.confirmSelect();
        //开始时间
        long start = System.currentTimeMillis();

        /**
         * deliveryTag:消息标识
         * multiple ：是否批量确认
         */
        ConfirmCallback ackCallback = (deliveryTag, multiple) ->{
            System.out.println("确认成功：" + deliveryTag);
        };

        ConfirmCallback nackCallback = (deliveryTag, multiple) ->{
            System.out.println("确认失败");
        };

        //设置消息监听器，监听消息状态
        channel.addConfirmListener(ackCallback, nackCallback);

        for (int i = 0; i < 1000; i++) {
            channel.basicPublish("", queneName, null, (i+"").getBytes());
        }

        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("时间：" + (end - start));
    }
}