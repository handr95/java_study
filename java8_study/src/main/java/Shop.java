import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Shop {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Getter
    private String shopNm;

    public Shop() {
    }

    public Shop(String shopNm) {
        this.shopNm = shopNm;
    }

    public double getPrice(String product) {
        return calculatePrice(product);
    }

    public Future<Double> getPriceAsync(String product){
        CompletableFuture<Double> futurePrice = new CompletableFuture<>();
        new Thread(() ->{
            try {
                double price = calculatePrice(product);
                futurePrice.complete(price);
            } catch(Exception e) {
                futurePrice.completeExceptionally(e);
            }
        }).start();

        return futurePrice;
    }

    public Future<Double> getPriceAsyncUseSupplyAsync(String product) {
        return CompletableFuture.supplyAsync(() -> {
            //logger.info("getPriceAsyncUseSupplyAsync");
            return calculatePrice(product);
        });
    }

    public double calculatePrice(String product) {
        Common.delay();
        /*if (product.equals("my favorite product")) {
            throw new RuntimeException("product not available");
        }*/
        return new Random().nextDouble() * product.charAt(0) + product.charAt(1);
    }



    public void doSomethingElse() {
        try {
            Thread.sleep(1000L);
            System.out.println("doSomethingElse");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
