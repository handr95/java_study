package conf;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;

import service.FrontDeskImpl;

@Configuration
@EnableTransactionManagement
public class FrontOfficeConfiguration {

    /*@Bean
    public FrontDeskImpl frontDesk() {
        return new FrontDeskImpl();
    }*/

    @Bean
    public ConnectionFactory connectionFactory() {
        return new ActiveMQConnectionFactory("tcp://localhost:61616");
    }

    @Bean
    public Queue destination() {
        return new ActiveMQQueue("mail.queue");
    }

    @Bean
    public JmsTemplate jmsTemplate() {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(connectionFactory());
        return jmsTemplate;
    }

    @Bean
    public FrontDeskImpl frontDesk() {
        FrontDeskImpl frontDesk = new FrontDeskImpl();
        frontDesk.setJmsTemplate(jmsTemplate());
        frontDesk.setDestination(destination());
        return frontDesk;
    }
}
