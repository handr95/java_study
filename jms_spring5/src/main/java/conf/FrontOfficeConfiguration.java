package conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import service.FrontDeskImpl;

@Configuration
public class FrontOfficeConfiguration {

    @Bean
    public FrontDeskImpl frontDesk() {
        return new FrontDeskImpl();
    }
}
