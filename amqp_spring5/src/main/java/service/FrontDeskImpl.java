package service;


import org.springframework.amqp.rabbit.core.RabbitGatewaySupport;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Random;

import model.Mail;

@Service
public class FrontDeskImpl extends RabbitGatewaySupport implements FrontDesk {

    private static final String QUEUE_NAME = "mail.queue";

    /*@Override
    public void sendMail(Mail mail) {
        ApplicationContext context = new AnnotationConfigApplicationContext(RabbitmqConfiguration.class);
        connectionFactory connectionFactory = context.getBean(connectionFactory.class);

        Channel channel = null;
        Connection connection = null;

        try  {
            connection = connectionFactory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            String message = new ObjectMapper().writeValueAsString(mail);
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (channel != null) {
                try {
                    channel.close();
                } catch (IOException | TimeoutException e) {
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (IOException e) {
                }
            }
        }
    }*/

    @Override
    public void sendMail(final Mail mail) {
        getRabbitOperations().convertAndSend(mail);
    }

    @Scheduled(initialDelay = 175, fixedDelay = 225)
    public void sendMailTrigger() {
        Random rnd = new Random();
        int countries = Locale.getISOCountries().length;
        String country = Locale.getISOCountries()[rnd.nextInt(countries)];

        sendMail(new Mail(String.valueOf(System.currentTimeMillis()), country, rnd.nextDouble()));
    }
}
