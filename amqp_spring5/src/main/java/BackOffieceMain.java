import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import config.RabbitmqConfiguration;

public class BackOffieceMain {

    public static void main(String[] args){
        ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(RabbitmqConfiguration.class);
/*
        BackOffice backOffice = context.getBean(BackOffice.class);
        backOffice.receiveMail();*/
    }
}

