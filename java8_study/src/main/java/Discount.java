import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Discount { ;
    Logger logger = LoggerFactory.getLogger(this.getClass());

    public enum Code {
        NONE(0), SILVER(5), GOLD(10), PLATINUM(15), DIAMOND(20);

        private final int percentage;

        Code(int percentage) {
            this.percentage = percentage;
        }
    }

    public static String applyDiscount(Quote quote) {
        /*String threadName = Thread.currentThread().getName();
        System.out.println(threadName + ": applyDiscount : " + quote.getShopNm());*/
        return quote.getShopNm() + " price is " + Discount.apply(quote.getPrice(), quote.getDiscountCode());
    }

    public static double apply(double price, Code code) {
        Common.delay();
        return (price * (100 - code.percentage) / 100);
    }
}
