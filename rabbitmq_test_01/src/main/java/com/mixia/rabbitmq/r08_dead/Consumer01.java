package com.mixia.rabbitmq.r08_dead;

import com.mixia.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.util.HashMap;
import java.util.Map;

/**
 *  死信交换机-消费者1
 *
 * @program: rabbitmq_test
 * @description
 * @author: jiny
 * @create: 2022-06-24 15:50
 **/
public class Consumer01 {
    //普通交换机名称
    private static final String NORMAL_EXCHANGE = "normal_exchange";
    //死信交换机名称
    private static final String DEAD_EXCHANGE = "dead_exchange";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();

        //声明普通和死信交换机
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

        //声明死信队列
        channel.queueDeclare("dead-queue", false, false, false, null);
        //绑定队列
        channel.queueBind("dead-queue", DEAD_EXCHANGE, "lisi");

        /**
         * arguments：
         * 队列中的消息什么时候会自动被删除？
         *
         * Message TTL(x-message-ttl)：设置队列中的所有消息的生存周期(统一为整个队列的所有消息设置生命周期), 也可以在发布消息的时候单独为某个消息指定剩余生存时间,单位毫秒, 类似于redis中的ttl，生存时间到了，消息会被从队里中删除，注意是消息被删除，而不是队列被删除， 特性Features=TTL, 单独为某条消息设置过期时间AMQP.BasicProperties.Builder properties = new AMQP.BasicProperties().builder().expiration(“6000”);
         * channel.basicPublish(EXCHANGE_NAME, “”, properties.build(), message.getBytes(“UTF-8”));
         *
         * Auto Expire(x-expires): 当队列在指定的时间没有被访问(consume, basicGet, queueDeclare…)就会被删除,Features=Exp
         *
         * Max Length(x-max-length): 限定队列的消息的最大值长度，超过指定长度将会把最早的几条删除掉， 类似于mongodb中的固定集合，例如保存最新的100条消息, Feature=Lim
         *
         * Max Length Bytes(x-max-length-bytes): 限定队列最大占用的空间大小， 一般受限于内存、磁盘的大小, Features=Lim B
         *
         * Dead letter exchange(x-dead-letter-exchange)： 当队列消息长度大于最大长度、或者过期的等，将从队列中删除的消息推送到指定的交换机中去而不是丢弃掉,Features=DLX
         *
         * Dead letter routing key(x-dead-letter-routing-key)：将删除的消息推送到指定交换机的指定路由键的队列中去, Feature=DLK
         *
         * Maximum priority(x-max-priority)：优先级队列，声明队列时先定义最大优先级值(定义最大值一般不要太大)，在发布消息的时候指定该消息的优先级， 优先级更高（数值更大的）的消息先被消费,
         *
         * Lazy mode(x-queue-mode=lazy)： Lazy Queues: 先将消息保存到磁盘上，不放在内存中，当消费者开始消费的时候才加载到内存中
         *
         * Master locator(x-queue-master-locator)
         */
        //正常队列绑定死信队列信息
        Map<String, Object> params = new HashMap<>();
        //当队列消息长度大于最大长度、或者过期的等，将从队列中删除的消息推送到指定的交换机中去而不是丢弃掉,Features=DLX
        params.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        //将删除的消息推送到指定交换机的指定路由键的队列中去, Feature=DLK
        params.put("x-dead-letter-routing-key", "lisi");
        //限定队列的消息的最大值长度，超过指定长度将会把最早的几条删除掉
        //params.put("x-max-length", 6);
        channel.queueDeclare("normal-queue", false, false, false, params);
        channel.queueBind("normal-queue", NORMAL_EXCHANGE, "zhangsan");

        System.out.println("等待接收消息......");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            if(message.equals("info_5")){
                System.out.println("Consumer01 接收到消息" + message + " 消息被拒绝");
                //requeue 设置为 false 代表拒绝重新入队 该队列如果配置了死信交换机将发送到死信队列中
                channel.basicReject(delivery.getEnvelope().getDeliveryTag(), false);
            }else{
                System.out.println("Consumer01 接收到消息" + message);
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
        };

        channel.basicConsume("normal-queue", deliverCallback, (consumerTag, sig) -> {});


    }



}
