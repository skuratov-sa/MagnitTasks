package com.skuratov.task1.util;

import com.skuratov.task1.model.constant.TypeDestination;
import jakarta.jms.Connection;
import jakarta.jms.Destination;
import jakarta.jms.JMSException;
import jakarta.jms.Session;
import org.apache.qpid.jms.JmsConnectionFactory;

public class Utils {

    public static Session createSession(String url, int sessionType) throws JMSException {
        var factory = new JmsConnectionFactory(url);
        Connection connection = factory.createConnection();
        connection.start();
        return connection.createSession(getValidSessionType(sessionType));
    }

    private static int getValidSessionType(int sessionType){
       if(sessionType < 0 || sessionType > 3){
           sessionType = 3;
       }
       return sessionType;
    }

    public static Destination createDestination(Session session, TypeDestination typeDestination) throws JMSException {
        return switch (typeDestination) {
            case TOPIC -> session.createTopic("Topic_session");
            default -> session.createQueue("Queue_session");
        };
    }
}
