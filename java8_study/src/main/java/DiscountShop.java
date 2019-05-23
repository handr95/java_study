import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DiscountShop {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Getter
    private String shopNm;

    public DiscountShop() {
    }

    public DiscountShop(String shopNm) {
        this.shopNm = shopNm;
    }

    public String getPrice(String product) {
        double price = calculatePrice(product);
                Discount.Code code = Discount.Code.values()[new Random().nextInt(Discount.Code.values().length)];
        logger.info("getPrice : " + shopNm);
        return String.format("%s:%.2f:%s", shopNm, price, code);
    }

    public double calculatePrice(String product) {
        Common.randomDelay();
        return new Random().nextDouble() * product.charAt(0) + product.charAt(1);
    }


}
