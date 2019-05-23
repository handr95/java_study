import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DIscountPriceFinderTest {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private List<DiscountShop> shopList
        = Arrays.asList(new DiscountShop("BestPrice"),
                        new DiscountShop("LetsSaveBig"),
                        new DiscountShop("MyFavoriteShop"),
                        new DiscountShop("BuyItAll"));

        /*Arrays.asList(new DiscountShop("BestPrice"), new DiscountShop("LetsSaveBig")
        , new DiscountShop("MyFavoriteShop"), new DiscountShop("BuyItAll"), new DiscountShop("ShopEasy")
        , new DiscountShop("MyFavoriteShop"), new DiscountShop("BuyItAll"), new DiscountShop("ShopEasy")
        , new DiscountShop("ShopEasy"));*/


    private final Executor executor = Executors.newFixedThreadPool(Math.min(shopList.size(), 100), new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        }
    });

    public List<String> findPrices(String product) {
        return shopList.stream()
            .map(shop -> shop.getPrice(product))
            .map(Quote::parse)
            .map(Discount::applyDiscount)
            .collect(Collectors.toList());
    }

    @Test
    public void findPrices_test() {
        long start = System.nanoTime();
        System.out.println(findPrices("myPhone27S"));
        long duration = ((System.nanoTime() - start) / 1_100_100);
        System.out.println("Done in " + duration + "ms");
        /**
         * [BestPrice price is 151.37, LetsSaveBig price is 171.8075, MyFavoriteShop price is 178.94400000000002, BuyItAll price is 190.342, ShopEasy price is 179.588]
         * Done in 9131ms
         */
    }

    public List<String> findPricesParallel(String product) {
        return shopList.parallelStream()
            .map(shop -> shop.getPrice(product))
            .map(Quote::parse)
            .map(Discount::applyDiscount)
            .collect(Collectors.toList());
    }

    @Test
    public void findPricesParallel_test() {
        long start = System.nanoTime();
        System.out.println(findPricesParallel("myPhone27S"));
        long duration = ((System.nanoTime() - start) / 1_100_100);
        System.out.println("Done in " + duration + "ms");
        /**
         * [BestPrice price is 120.25800000000001, LetsSaveBig price is 131.23149999999998, MyFavoriteShop price is 212.79, BuyItAll price is 158.38, ShopEasy price is 169.60049999999998]
         * Done in 1864ms
         */
    }

    public List<String> findPricesCompletableFuture(String product) {
        List<CompletableFuture<String>> pricefutres = shopList.stream()
            .map(shop -> CompletableFuture.supplyAsync(() -> shop.getPrice(product), executor))
            .map(future -> future.thenApply(Quote::parse))
            .map(future -> future.thenCompose(quote -> CompletableFuture.supplyAsync(() -> Discount.applyDiscount(quote), executor)))
            .collect(Collectors.toList());

        return pricefutres.stream()
            .map(CompletableFuture::join)
            .collect(Collectors.toList());
    }

    @Test
    public void findPricesCompletableFuture_test() {
        long start = System.nanoTime();
        System.out.println(findPricesCompletableFuture("myPhone27S"));
        long duration = ((System.nanoTime() - start) / 1_100_100);
        System.out.println("Done in " + duration + "ms");
    }

    public Stream<CompletableFuture<String>> findPricesStream(String product) {
        return shopList.stream()
            .map(shop -> CompletableFuture.supplyAsync(() -> shop.getPrice(product), executor))
            .map(future -> future.thenApply(Quote::parse))
            .map(future -> future.thenCompose(quote -> CompletableFuture.supplyAsync(() -> Discount.applyDiscount(quote), executor)));
    }

    @Test
    public void printPricesStream() {
        long start = System.nanoTime();
        CompletableFuture[] futures = findPricesStream("myPhone27S")
            .map(f -> f.thenAccept(s -> System.out.println(s + " (done in " + ((System.nanoTime() - start) / 1_000_000) + " msecs)")))
            .toArray(size -> new CompletableFuture[size]);
        CompletableFuture.allOf(futures).join();
        System.out.println("All shops have now responded in " + ((System.nanoTime() - start) / 1_000_000) + " msecs");
    }


    public void printPricesStream_2(String product) {
        long start = System.nanoTime();
        List<CompletableFuture<Quote>> pricefutres = shopList.stream()
            .map(shop -> CompletableFuture.supplyAsync(() -> shop.getPrice(product), executor))
            .map(future -> future.thenApply(Quote::parse))
            .collect(Collectors.toList());

        CompletableFuture[] completableFutures = pricefutres.toArray(new CompletableFuture[0]);
        try {
            Void aVoid = CompletableFuture.allOf(completableFutures).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        //CompletableFuture.anyOf(completableFutures).get();
        //return CompletableFuture.allOf(completableFutures).join().stream().map(CompletableFuture::join);

        /*CompletableFuture.allOf(pricefutres.toArray()).join();
        System.out.println("All shops have now responded in " + ((System.nanoTime() - start) / 1_000_000) + " msecs");*/
    }

    @Test
    public void test(){
        logger.info("1");
        printPricesStream_2("test");
        logger.info("2");
    }
}
