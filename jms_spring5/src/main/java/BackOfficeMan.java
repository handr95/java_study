import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import conf.BackOfficeConfiguration;
import service.BackOffice;

public class BackOfficeMan {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(BackOfficeConfiguration.class);

        BackOffice backOffice = context.getBean(BackOffice.class);
        System.out.println(backOffice.receiveMail().toString());
    }
}
