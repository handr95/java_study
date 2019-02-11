import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import conf.FrontOfficeConfiguration;
import model.Mail;
import service.FrontDesk;

public class FrontOfficeMain {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(FrontOfficeConfiguration.class);

        FrontDesk frontDesk = context.getBean(FrontDesk.class);
        frontDesk.sendMail(new Mail("1234", "US", 1.5));
    }
}
