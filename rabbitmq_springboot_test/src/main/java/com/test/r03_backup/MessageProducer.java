package com.test.r03_backup;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.UUID;

/**
 * @program: rabbitmq_test
 * @description
 * @author: jiny
 * @create: 2022-06-30 17:12
 **/
@Slf4j
@RestController
public class MessageProducer implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback{
    public static final String CONFIRM_EXCHANGE_NAME = "backup.confirm.exchange";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    private void init(){
        //设置确认消息交给谁处理
        rabbitTemplate.setConfirmCallback(this);
        /**
         * true：
         * 交换机无法将消息进行路由时，会将该消息返回给生产者
         * false：
         * 如果发现消息无法进行路由，则直接丢弃
         */
        //可以理解为强制性的消息消费成功
        rabbitTemplate.setMandatory(true);
        //设置回退消息交给谁处理
        rabbitTemplate.setReturnsCallback(this);
    }

    @GetMapping("backup/sendMessage/{message}")
    public void sendMessage(@PathVariable String message){
        CorrelationData correlationData1 = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(CONFIRM_EXCHANGE_NAME, "key1", message + "key1", correlationData1);
        log.info("发送消息 id 为:{}内容为{}", correlationData1.getId(), message+"key1");
        CorrelationData correlationData2 = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(CONFIRM_EXCHANGE_NAME, "key2", message + "key2", correlationData2);
        log.info("发送消息 id 为:{}内容为{}", correlationData2.getId(), message+"key2");
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = null == correlationData ? "" : correlationData.getId();
        if(ack){
            //确认成功
            log.info("交换机收到消息确认成功, id:{}", id);
        }else{
            //确认失败
            log.error("消息 id:{}未成功投递到交换机,原因是:{}", id, cause);
        }
    }

    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        log.info("消息:{}被服务器退回，退回原因:{}, 交换机是:{}, 路由 key:{}",
                new String(returnedMessage.getMessage().getBody()),returnedMessage.getReplyText(), returnedMessage.getExchange(), returnedMessage.getRoutingKey());
    }
}
