package com.skuratov.springeactivewmq.model;

import com.skuratov.springeactivewmq.repo.MsgRepository;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class Subscriber {

    private MsgRepository repo;

    @SneakyThrows
    @JmsListener(destination = "standalone.queue")
    public void consume(ActiveMQTextMessage message) {
        repo.addMessage(message);
        log.info("Incoming message : {}", message.getText());
    }
}
