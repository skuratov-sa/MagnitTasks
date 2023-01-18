package com.skuratov.task1.model;

import com.skuratov.task1.model.constant.TypeDestination;
import com.skuratov.task1.util.Utils;
import jakarta.jms.Destination;
import jakarta.jms.JMSException;
import jakarta.jms.MessageProducer;
import jakarta.jms.Session;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;


@Builder
@Slf4j
public class Producer extends Thread {

    private final String url;
    private final TypeDestination typeDestination;
    private final int sessionType;
    private final int deliveryMode;

    @Override
    public void run() {
        try (Session session = Utils.createSession(url, sessionType);
             var msgProducer = createProducer(session, typeDestination, deliveryMode)) {
            for (int i = 0; i < 3; i++) {
                String text = "Msg_" + i;
                var message = session.createTextMessage(text);
                log.info(String.format("Producer_%d msg: %s", Thread.currentThread().getId(), text));
                msgProducer.send(message);
            }
            if (sessionType == Session.SESSION_TRANSACTED) {
                session.commit();
            }
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }

    private MessageProducer createProducer(Session session,
                                           TypeDestination typeDestination,
                                           int deliveryMode) throws JMSException {
        var destination = Utils.createDestination(session, typeDestination);
        var msgProducer = session.createProducer(destination);
        msgProducer.setDeliveryMode(deliveryMode);
        return msgProducer;
    }
}