package study.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

import study.dao.VehicleDao;

@Configuration
public class SpringConfiguration {
    @Bean
    public VehicleDao springConfiguration(){
        return new VehicleDao(dataSource());
    }

    private DataSource dataSource() {
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        return null;
    }
}
