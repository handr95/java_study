package study.service;

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

import study.config.RabbitmqConfiguration;
import study.model.Mail;

@Service
public class BackOfficeImpl implements BackOffice {

    private static final String QUEUE_NAME = "mail.queue";

    private MailListener mailListener = new MailListener();


    @Override
    public Mail receiveMail() {
        ApplicationContext context = new AnnotationConfigApplicationContext(RabbitmqConfiguration.class);
        ConnectionFactory connectionFactory = context.getBean(ConnectionFactory.class);

        Channel channel = null;

        try (Connection connection = connectionFactory.newConnection()) {
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

        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (channel != null) {
                try {
                    channel.close();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
