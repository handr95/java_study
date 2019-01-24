package study.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:database.properties")
public class DataBasesProperties {

    @Value("${postgresql.driverClass}")
    private String driverClass;

    @Value("${postgresql.url}")
    private String url;

    @Value("${postgresql.user}")
    private String user;

    @Value("${postgresql.password}")
    private String password;
}
