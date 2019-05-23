import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CompletableFutureTest {

    Logger logger = LoggerFactory.getLogger(this.getClass());


    @Test
    public void getPriceSync() {
        Shop shop = new Shop("Best Shop");
        long start = System.nanoTime();
        long invocationTime = ((System.nanoTime() - start) / 1_100_100);
        System.out.println("Invocation returned after " + invocationTime + "ms");

        double price = shop.getPrice("my favorite product");
        System.out.printf("price is %.2f%n", price);

        shop.doSomethingElse();

        long retrievalTime = ((System.nanoTime() - start) / 1_100_100);
        System.out.println("Price returned after " + retrievalTime + "ms");
        /**
         * Invocation returned after 0ms
         * price is 223.91
         * doSomethingElse
         * Price returned after 181ms
         */
    }

    @Test
    public void getPriceAsync(){
        Shop shop = new Shop("Best Shop");
        long start = System.nanoTime();
        Future<Double> futurePrice = shop.getPriceAsync("my favorite product");
        //Future<Double> futurePrice = shop.getPriceAsyncUseSupplyAsync("my favorite product");
        long invocationTime = ((System.nanoTime() - start) / 1_100_100);
        System.out.println("Invocation returned after " + invocationTime + "ms");

        shop.doSomethingElse();

        try {
            double price = futurePrice.get();
            System.out.printf("price is %.2f%n", price);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        long retrievalTime = ((System.nanoTime() - start) / 1_100_100);
        System.out.println("Price returned after " + retrievalTime + "ms");

        /**
         * Invocation returned after 0ms
         * doSomethingElse
         * price is 156.06
         * Price returned after 91ms
         */
    }

    public Future<Double> futurePriceThenCombine(Shop shop, String product) {
        Future<Double> futurePriceInUSD = CompletableFuture.supplyAsync(() -> shop.getPrice(product))
            .thenCombine(
                CompletableFuture.supplyAsync(
                    () -> ExchangeService.getRate(ExchangeService.Money.EUR, ExchangeService.Money.USD)),
            (price, rate) -> price * rate);
        return futurePriceInUSD;
    }

    @Test
    public void getPriceAsyncThenCombine_test(){
        Shop shop = new Shop("Best Shop");
        //Future<Double> futurePrice = shop.getPriceAsync("my favorite product");
        Future<Double> futurePrice = futurePriceThenCombine(shop, "my favorite product");
        long start = System.nanoTime();
        long invocationTime = ((System.nanoTime() - start) / 1_100_100);
        System.out.println("Invocation returned after " + invocationTime + "ms");

        shop.doSomethingElse();

        try {
            double price = futurePrice.get();
            System.out.printf("price is %.2f%n", price);
            logger.info("price");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        long retrievalTime = ((System.nanoTime() - start) / 1_100_100);
        System.out.println("Price returned after " + retrievalTime + "msc");

        /**
         * Invocation returned after 0ms
         * doSomethingElse
         * price is 91.34
         * Price returned after 919ms
         */
    }

    public Future<Double> futurePriceThenCombine_v2(Shop shop, String product) {
        ExecutorService executor = Executors.newCachedThreadPool();
        final Future<Double> futureRate = executor.submit(
            new Callable<Double>() {
                @Override
                public Double call() throws Exception {
                    return ExchangeService.getRate(ExchangeService.Money.EUR, ExchangeService.Money.USD);
                }
            }
        );

        final Future<Double> futurePriceInUSD = executor.submit(
            new Callable<Double>() {
                @Override
                public Double call() throws Exception {
                    double priceInEUR = shop.getPrice(product);
                    return priceInEUR * futureRate.get();
                }
            }
        );

        return futurePriceInUSD;
    }

}
