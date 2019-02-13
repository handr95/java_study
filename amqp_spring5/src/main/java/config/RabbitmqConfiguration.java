package config;

import com.rabbitmq.client.ConnectionFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import service.BackOffice;
import service.BackOfficeImpl;
import service.FrontDesk;
import service.FrontDeskImpl;

@PropertySource("classpath:database.properties")
@Configuration
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
    public ConnectionFactory ConnectionFactory() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setPort(port);
        return connectionFactory;
    }

    /*@Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(ConnectionFactory());
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        rabbitTemplate.setRoutingKey("mail.queue");
        return rabbitTemplate;
    }*/

    @Bean
    public FrontDesk frontOffice() {
        FrontDeskImpl frontDesk = new FrontDeskImpl();
        //frontDesk.setRabbitOperations(rabbitTemplate());
        return frontDesk;
    }

    @Bean
    public BackOffice backOffice() {
        BackOfficeImpl backOffice = new BackOfficeImpl();
        //frontDesk.setRabbitOperations(rabbitTemplate());
        return backOffice;
    }
}
