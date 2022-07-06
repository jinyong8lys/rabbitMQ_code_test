package com.test.r01_delayedqueue;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @program: rabbitmq_test
 * @description
 * @author: jiny
 * @create: 2022-06-29 16:37
 **/
@Slf4j
@RestController
public class Producter {
    public static final String DELAYED_EXCHANGE_NAME = "delayed.exchange";
    public static final String DELAYED_ROUTING_KEY = "delayed.routingkey";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("sendDelayMsg/{message}/{delayTime}")
    public void sendMsg(@PathVariable String message, @PathVariable Integer delayTime){
        rabbitTemplate.convertAndSend(DELAYED_EXCHANGE_NAME, DELAYED_ROUTING_KEY, message,
                correlationData -> {
                    correlationData.getMessageProperties().setDelay(delayTime);
                return correlationData;
        });
        log.info("当前时间：{},发送一条延迟{}毫秒的信息给队列delayed.queue:{}", new Date(),delayTime, message);
    }


}
