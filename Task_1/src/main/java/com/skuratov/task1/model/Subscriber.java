package com.skuratov.task1.model;

import com.skuratov.task1.model.constant.TypeDestination;
import com.skuratov.task1.util.Utils;
import jakarta.jms.*;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder
public class Subscriber extends Thread {

    private final String url;
    private final TypeDestination typeDestination;
    private final int sessionType;
    private static boolean isClose;


    @Override
    public void run() {
        try (var session = Utils.createSession(url, sessionType);
             var consumer = createConsumer(session, typeDestination)) {
            String text;
            do {
                Message msg = consumer.receive();
                text = ((TextMessage) msg).getText();
                log.info(String.format("Subscriber_%d -> %s",
                        Thread.currentThread().getId(), text));
            } while (!isClose);
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }

    private MessageConsumer createConsumer(Session session, TypeDestination typeDestination) throws JMSException {
        var destination = Utils.createDestination(session, typeDestination);
        return session.createConsumer(destination);
    }

    public static void close(){
        isClose = true;
    }
}
