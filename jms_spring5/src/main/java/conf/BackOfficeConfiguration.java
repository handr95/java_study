package conf;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;

import service.BackOfficeImpl;

@Configuration
@EnableTransactionManagement
public class BackOfficeConfiguration {
    /*@Bean
    public BackOfficeImpl backDesk() {
        return new BackOfficeImpl();
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
        jmsTemplate.setReceiveTimeout(10000);   //타임아웃 시간 (밀리 초 단위), 이시간이 지나도록 도착한 메시지가 없다면 null 반환
        return jmsTemplate;
    }

    @Bean
    public BackOfficeImpl backOffice() {
        BackOfficeImpl backOffice = new BackOfficeImpl();
        backOffice.setDestination(destination());
        backOffice.setJmsTemplate(jmsTemplate());
        return backOffice;
    }
}
