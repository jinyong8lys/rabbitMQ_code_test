package com.mixia.rabbitmq.r09_ttl;

import com.mixia.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * 延迟队列-消费者1
 *
 * @program: rabbitmq_test
 * @description
 * @author: jiny
 * @create: 2022-06-27 15:17
 **/
public class Consumer01 {
    //交换机X
    public static final String X_EXCHANGE = "X";
    //队列A
    public static final String QUEUE_A = "QA";
    //队列B
    public static final String QUEUE_B = "QB";

    public static final String QUEUE_C = "QC";

    //死信交换机
    public static final String Y_DEAD_LETTER_EXCHANGE = "Y";
    //死信队列
    public static final String DEAD_LETTER_QUEUE = "QD";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();

        //声明死信交换机并绑定队列
        channel.exchangeDeclare(Y_DEAD_LETTER_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.queueDeclare(DEAD_LETTER_QUEUE, false, false, false, null);
        channel.queueBind(DEAD_LETTER_QUEUE, Y_DEAD_LETTER_EXCHANGE, "YD");

        //声明普通交换机
        channel.exchangeDeclare(X_EXCHANGE, BuiltinExchangeType.DIRECT);

        Map<String, Object> argsMAp = new HashMap<>();
        //设置绑定的死信交换机
        argsMAp.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);
        //指定交换机的指定路由键
        argsMAp.put("x-dead-letter-routing-key", "YD");

        //声明队列A
        channel.queueDeclare(QUEUE_C, false, false, false, argsMAp);
        channel.queueBind(QUEUE_C, X_EXCHANGE, "XC");

        System.out.println("Consumer01-QC等待接收消息......");

        DeliverCallback deliverCallback_C = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "utf-8");
            System.out.println("Consumer01-QC接收到消息：" + message);
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };
        channel.basicConsume(QUEUE_C, deliverCallback_C, (consumerTag, sig) -> {});

        //设置队列中的所有消息的生存周期
        argsMAp.put("x-message-ttl", 10000);
        //声明队列A
        channel.queueDeclare(QUEUE_A, false, false, false, argsMAp);
        channel.queueBind(QUEUE_A, X_EXCHANGE, "XA");

        System.out.println("Consumer01-QA等待接收消息......");

        DeliverCallback deliverCallback_A = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "utf-8");
            System.out.println("Consumer01-QA接收到消息：" + message);
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };
        channel.basicConsume(QUEUE_A, deliverCallback_A, (consumerTag, sig) -> {});

        //声明队列A
        //设置队列中的所有消息的生存周期
        argsMAp.put("x-message-ttl", 40000);
        channel.queueDeclare(QUEUE_B, false, false, false, argsMAp);
        channel.queueBind(QUEUE_B, X_EXCHANGE, "XB");

        System.out.println("Consumer01-QB等待接收消息......");

        DeliverCallback deliverCallback_B = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "utf-8");
            System.out.println("Consumer01-QB接收到消息：" + message);
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };
        channel.basicConsume(QUEUE_B, deliverCallback_B, (consumerTag, sig) -> {});
    }


}
