package config;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import service.BackOffice;
import service.BackOfficeImpl;
import service.FrontDesk;
import service.FrontDeskImpl;
import service.MailListener;

@PropertySource("classpath:database.properties")
@Configuration
@EnableRabbit
public class RabbitmqConfiguration {

    @Value("${rabbitmq.host}")
    private String host;

    @Value("${rabbitmq.username}")
    private String username;

    @Value("${rabbitmq.password}")
    private String password;

    @Value("${rabbitmq.port}")
    private int port;


    @Bean
    public ConnectionFactory connectionFactory() {  //
        //connectionFactory connectionFactory = new connectionFactory();   //com.rabbitmq.client.connectionFactory
        //connectionFactory.setHost(host);
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host); //org.springframework.amqp.rabbit.connection.connectionFactory
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setPort(port);
        return connectionFactory;
    }

    @Bean
    public FrontDesk frontOffice() {
        FrontDeskImpl frontDesk = new FrontDeskImpl();
        frontDesk.setRabbitOperations(rabbitTemplate());
        return frontDesk;
    }

    @Bean
    public BackOffice backOffice() {
        BackOfficeImpl backOffice = new BackOfficeImpl();
        //backOffice.setRabbitOperations(rabbitTemplate());
        return backOffice;
    }


    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory());
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        rabbitTemplate.setRoutingKey("mail.queue");
        return rabbitTemplate;
    }

    @Bean
    public RabbitListenerContainerFactory rabbitListenerContainerFactory() {
        SimpleRabbitListenerContainerFactory containerFactory = new SimpleRabbitListenerContainerFactory();
        containerFactory.setConnectionFactory(connectionFactory());
        containerFactory.setMessageConverter(new Jackson2JsonMessageConverter());
        return containerFactory;
    }

    @Bean
    public MailListener mailListener() {
        return new MailListener();
    }
}
