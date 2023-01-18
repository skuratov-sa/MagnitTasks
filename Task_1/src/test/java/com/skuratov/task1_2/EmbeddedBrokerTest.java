package com.skuratov.task1_2;

import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.apache.activemq.junit.EmbeddedActiveMQBroker;
import org.junit.Test;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static jakarta.jms.Session.AUTO_ACKNOWLEDGE;
import static org.junit.Assert.fail;

@Slf4j
public class EmbeddedBrokerTest {

    private String URL = "vm://localhost?broker.persistent=false";

    @Test
    public void runBroker() {
        var broker = new BrokerService();
        try {
            broker.addConnector(URL);
            broker.start();
        } catch (Exception e) {
            fail();
        }

        var connectionFactory = new ActiveMQConnectionFactory(URL);
        for (int i = 0; i < 3; i++) {
            createConsumer(connectionFactory).start();
        }
        createProducer(connectionFactory).start();

        try {
            Thread.sleep(1000);
            broker.stop();
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void runEmbeddedBroker() {
        var broker = new EmbeddedActiveMQBroker();
        try {
            broker.start();
        } catch (Exception e) {
            fail();
        }

        var connectionFactory = broker.createConnectionFactory();
        ExecutorService service = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 3; i++) {
            service.execute(createConsumer(connectionFactory));
        }
        createProducer(connectionFactory).start();

        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            fail();
        }finally {
            service.shutdown();
        }
    }

    private Thread createProducer(ActiveMQConnectionFactory factory) {
        return new Thread(() -> {
            try {
                Connection connection = factory.createConnection();
                connection.start();
                Session session1 = connection.createSession(false, AUTO_ACKNOWLEDGE);
                var destination = session1.createTopic("Topic");
                var producer = session1.createProducer(destination);
                var msg = session1.createTextMessage("Hello Stas");
                producer.send(msg);
                log.info(String.format("Producer_%d send: %s", Thread.currentThread().getId(), msg.getText()));
                producer.close();
            } catch (JMSException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private Thread createConsumer(ActiveMQConnectionFactory factory) {
        return new Thread(() -> {
            try {
                Connection connection = factory.createConnection();
                connection.start();
                Session session = connection.createSession(false, AUTO_ACKNOWLEDGE);
                var destinationConsumer = session.createTopic("Topic");
                var consumer = session.createConsumer(destinationConsumer);
                var msgAns = consumer.receive();
                var text = ((ActiveMQTextMessage) msgAns).getText();
                log.info(String.format("Consumer_%d -> %s", Thread.currentThread().getId(), text));
                consumer.close();
            } catch (JMSException e) {
                throw new RuntimeException(e);
            }
        });
    }


}
