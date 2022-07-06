package com.test.r02_publish_confirm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @program: rabbitmq_test
 * @description
 * @author: jiny
 * @create: 2022-06-30 16:40
 **/
@Slf4j
@Component
public class ConfirmConsumer {
    public static final String CONFIRM_QUEUE_NAME = "confirm.queue";

    @RabbitListener(queues = CONFIRM_QUEUE_NAME)
    public void receiveMsg(Message message){
        String msg = new String(message.getBody());
        log.info("接受到队列 confirm.queue 消息:{}",msg);
    }
}
