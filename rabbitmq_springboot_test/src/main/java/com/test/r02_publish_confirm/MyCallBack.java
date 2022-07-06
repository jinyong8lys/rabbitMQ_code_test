package com.test.r02_publish_confirm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * @program: rabbitmq_test
 * @description
 * @author: jiny
 * @create: 2022-06-30 15:19
 **/
@Slf4j
@Component
public class MyCallBack implements RabbitTemplate.ConfirmCallback {

    /**
     * 交换机不管是否收到消息的一个回调方法
     * CorrelationData
     * 消息相关数据
     * ack
     * 交换机是否收到消息
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean b, String s) {
        String id = correlationData != null ? correlationData.getId() : "";
        if(b){
            log.info("交换机已经收到 id 为:{}的消息", id);
        }else{
            log.info("交换机还未收到 id 为:{}消息,由于原因:{}", id, s);
        }
    }
}
