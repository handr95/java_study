package conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import service.BackDeskImpl;

@Configuration
public class BackOfficeConfiguration {
    @Bean
    public BackDeskImpl backDesk() {
        return new BackDeskImpl();
    }
}
