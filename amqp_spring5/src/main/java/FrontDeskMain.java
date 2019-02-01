import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

import config.RabbitmqConfiguration;
import model.Mail;
import service.FrontDesk;

public class FrontDeskMain {

    public static void main(String[] args) throws IOException {
        ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(RabbitmqConfiguration.class);

        FrontDesk frontDesk = context.getBean(FrontDesk.class);
        frontDesk.sendMail(new Mail("1234", "US", 1.5));

        System.in.read();
        context.close();
    }

}
