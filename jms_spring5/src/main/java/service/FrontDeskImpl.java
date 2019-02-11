package service;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.Session;

import model.Mail;

public class FrontDeskImpl implements FrontDesk {

    @Override
    public void sendMail(Mail mail) {
        ConnectionFactory cf = new ActiveMQConnectionFactory("tcp://localhost:61616");
        Destination destination = new ActiveMQQueue("mail.queue");

        Connection conn = null;
        try {
            conn = cf.createConnection();
            Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer producer = session.createProducer(destination);


            MapMessage message = session.createMapMessage();
            message.setString("mailId", mail.getMailId());
            message.setString("country", mail.getCountry());
            message.setDouble("weight", mail.getWeight());

            producer.send(message);

            session.close();
        } catch (JMSException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) { //JMS 연결이 닫히면 그때마다 기존 세션도 모두 함께 닫히기 때문에 JMS 연결만 제대로 닫아주면 된다.
                try {
                    conn.close();
                } catch (JMSException e){

                }
            }
        }
    }

}
