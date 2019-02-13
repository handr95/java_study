package service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import model.Mail;

public class MailListener implements MessageListener {

    @Override
    public void onMessage(Message message) {
        MapMessage mapMessage = (MapMessage) message;
        try {
            Mail mail = new Mail();
            mail.setMailId(mapMessage.getString("mailId"));
            mail.setCountry(mapMessage.getString("country"));
            mail.setCountry(mapMessage.getString("weight"));
            displayMail(mail);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    /*public void displayMail(Mail mail) {
        System.out.println("Mail #"+mail.getMailId() + "received");
    }*/

    @RabbitListener(queues = "mail.queue")
    public void displayMail(Mail mail) {
        System.out.println("Received : " + mail.toString());
    }
}
