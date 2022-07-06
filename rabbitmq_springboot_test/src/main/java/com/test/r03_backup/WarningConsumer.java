package com.test.r03_backup;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @program: rabbitmq_test
 * @description
 * @author: jiny
 * @create: 2022-07-01 14:31
 **/
@Slf4j
@Component
public class WarningConsumer {
    public static final String WARNING_QUEUE_NAME = "backup.warning.queue";

    @RabbitListener(queues = WARNING_QUEUE_NAME)
    public void receiveWarningMsg(Message message){
        String msg  = new String(message.getBody());
        log.error("报警发现不可路由消息： {}", msg);
    }

}
