package com.skuratov.task1;

import com.skuratov.task1.model.Producer;
import com.skuratov.task1.model.Subscriber;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.skuratov.task1.model.constant.TypeDestination.QUEUE;
import static com.skuratov.task1.model.constant.TypeDestination.TOPIC;
import static jakarta.jms.DeliveryMode.NON_PERSISTENT;
import static jakarta.jms.DeliveryMode.PERSISTENT;
import static jakarta.jms.Session.*;

@Slf4j
public class ProducerRunnerTest {

    private final String URL = "amqp://localhost:5672";

    @Test
    public void runSessionAuto() throws InterruptedException {
        log.info("-------------------------------------");
        log.info("Session -> AUTO_ACKNOWLEDGE");
        ExecutorService service = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 3; i++) {
            service.execute(Subscriber.builder()
                    .url(URL)
                    .sessionType(AUTO_ACKNOWLEDGE)
                    .typeDestination(TOPIC)
                    .build()
            );
        }

        Producer producer = Producer.builder()
                .url(URL)
                .typeDestination(TOPIC)
                .sessionType(AUTO_ACKNOWLEDGE)
                .deliveryMode(PERSISTENT)
                .build();
        producer.start();
        Thread.sleep(2_000);
        service.shutdown();
        Subscriber.close();
    }

    @Test
    public void runSessionTransient() throws InterruptedException {
        log.info("-------------------------------------");
        log.info("\nSession -> SESSION_TRANSACTED");
        ExecutorService service = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 3; i++) {
            service.execute(Subscriber.builder()
                    .url(URL)
                    .sessionType(SESSION_TRANSACTED)
                    .typeDestination(TOPIC)
                    .build()
            );
        }

        var producer = Producer.builder()
                .url(URL)
                .typeDestination(TOPIC)
                .sessionType(SESSION_TRANSACTED)
                .deliveryMode(NON_PERSISTENT)
                .build();
        producer.start();

        Thread.sleep(1_000);
        Subscriber.close();
        service.shutdown();
    }

    @Test
    public void runSessionClientAcknowledge() throws InterruptedException {
        log.info("-------------------------------------");
        log.info("\nSession -> CLIENT_ACKNOWLEDGE");
        ExecutorService service = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 3; i++) {
            service.execute(Subscriber.builder()
                    .url(URL)
                    .sessionType(CLIENT_ACKNOWLEDGE)
                    .typeDestination(TOPIC)
                    .build()
            );
        }

        Producer producer = Producer.builder()
                .url(URL)
                .typeDestination(TOPIC)
                .sessionType(CLIENT_ACKNOWLEDGE)
                .deliveryMode(NON_PERSISTENT)
                .build();
        producer.start();

        Thread.sleep(1_000);
        Subscriber.close();
        service.shutdown();
    }

    @Test
    public void runSessionDupsAcknowledge() throws InterruptedException {
        log.info("-------------------------------------");
        log.info("\nSession -> DUPS_OK_ACKNOWLEDGE");
        ExecutorService service = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 3; i++) {
            service.execute(Subscriber.builder()
                    .url(URL)
                    .sessionType(DUPS_OK_ACKNOWLEDGE)
                    .typeDestination(TOPIC)
                    .build()
            );
        }

        Producer producer = Producer.builder()
                .url(URL)
                .typeDestination(TOPIC)
                .sessionType(DUPS_OK_ACKNOWLEDGE)
                .deliveryMode(PERSISTENT)
                .build();
        producer.start();

        Thread.sleep(1_000);
        
        service.shutdown();
    }

    @Test
    public void runQueueDestination() throws InterruptedException {
        log.info("-------------------------------------");
        log.info("\nDestination -> QUEUE");
        ExecutorService service = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 3; i++) {
            service.execute(Subscriber.builder()
                    .url(URL)
                    .sessionType(DUPS_OK_ACKNOWLEDGE)
                    .typeDestination(QUEUE)
                    .build()
            );
        }

        Producer producer = Producer.builder()
                .url(URL)
                .typeDestination(QUEUE)
                .sessionType(DUPS_OK_ACKNOWLEDGE)
                .deliveryMode(PERSISTENT)
                .build();
        producer.start();

        Thread.sleep(1_000);
        Subscriber.close();
        service.shutdown();
    }

}
