package study.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

import study.dao.VehicleDao;

@PropertySource("classpath:database.properties")
@Configuration
public class DatabasesConfiguration {

    @Value("${postgresql.driver.class}")
    private String driverClass;

    @Value("${postgresql.url}")
    private String url;

    @Value("${postgresql.user}")
    private String user;

    @Value("${postgresql.password}")
    private String password;


    @Bean
    public VehicleDao springConfiguration(){
        return new VehicleDao(dataSource());
    }

    private DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClass);
        dataSource.setUsername(user);
        dataSource.setUrl(url);
        dataSource.setPassword(password);
        return dataSource;
    }
}
