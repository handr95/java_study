package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import javax.annotation.PreDestroy;

import config.RabbitmqConfiguration;
import model.Mail;

@Service
public class BackOfficeImpl implements BackOffice {

    private static final String QUEUE_NAME = "mail.queue";

    private MailListener mailListener = new MailListener();

    private Connection connection = null;
    private Channel channel = null;

    @Override
    public Mail receiveMail() {
        ApplicationContext context = new AnnotationConfigApplicationContext(RabbitmqConfiguration.class);
        ConnectionFactory connectionFactory = context.getBean(ConnectionFactory.class);

        try {
            connection = connectionFactory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery (String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    Mail mail = new ObjectMapper().readValue(body, Mail.class);
                    mailListener.displayMail(mail);
                }
            };
            channel.basicConsume(QUEUE_NAME, true, consumer);
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @PreDestroy
    public void destory() {
        if (this.connection != null) {
            try {
                this.connection.close();
            } catch (IOException e) {

            }
        }
    }
}
