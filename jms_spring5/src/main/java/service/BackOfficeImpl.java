package service;


import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.JmsUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;

import model.Mail;

public class BackOfficeImpl implements BackOffice {

    /*@Override
    public Mail receiveMail() {
        ConnectionFactory cf = new ActiveMQConnectionFactory("tcp://localhost:61616");
        Destination destination = new ActiveMQQueue("mail.queue");
        Connection conn = null;

        try {
            conn = cf.createConnection();
            Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageConsumer consumer = session.createConsumer(destination);

            conn.start();
            MapMessage message = (MapMessage) consumer.receive();
            Mail mail = new Mail();
            mail.setMailId(message.getString("mailId"));
            mail.setCountry(message.getString("country"));
            mail.setWeight(message.getDouble("weight"));

            session.close();
            return mail;
        } catch (JMSException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (JMSException e) {
                }
            }
        }
    }*/

    private JmsTemplate jmsTemplate;
    private Destination destination;

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    @Transactional
    @Override
    public Mail receiveMail() {
        MapMessage message = (MapMessage) jmsTemplate.receive(destination);
        //jmsTemplate.setDefaultDestination(기본 목적지) -> jmsTemplate.receive() 목적지 입력 안해도 됨
        //jmsTemplate.receiveAndConvert()
        try {
            if (message == null) {
                return null;
            }
            Mail mail = new Mail();
            mail.setMailId(message.getString("mailId"));
            mail.setCountry(message.getString("country"));
            mail.setWeight(message.getDouble("weight"));

            return mail;
        } catch (JMSException e) {
            throw JmsUtils.convertJmsAccessException(e);
        }
    }
}
