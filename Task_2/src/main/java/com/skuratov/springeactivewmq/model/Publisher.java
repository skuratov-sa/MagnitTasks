package com.skuratov.springeactivewmq.model;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.jms.Queue;

@Slf4j
@AllArgsConstructor
@Component
public class Publisher {

    private final JmsTemplate jmsTemplate;
    private final Queue queue;

    @SneakyThrows
    @Scheduled(initialDelay = 2, fixedRate = 2_500)
    public void sendMessage() {
        var msg = new ActiveMQTextMessage();
        double rundNum = Math.random() * 100;
        msg.setText("My message number " + rundNum);
        msg.setUserID("user-" + (Math.floor(rundNum)));
        msg.setGroupID("group-" + Math.floor(rundNum + 10));

        jmsTemplate.convertAndSend(queue, msg);
        log.info("Publisher_{} send msg", Thread.currentThread().getId());
    }
}
