import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import lombok.extern.slf4j.Slf4j;

import static java.util.stream.Collectors.toList;

@Slf4j
public class PriceFinderTest {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    private List<Shop> shopList = Arrays.asList(new Shop("BestPrice"), new Shop("LetsSaveBig"), new Shop("MyFavoriteShop"), new Shop("BuyItAll"));

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
                .map(shop -> String.format("%s price is %.2f", shop.getShopNm(), shop.getPrice(product)))
                .collect(toList());
    }

    @Test
    public void getPriceStream_test() {
        long start = System.nanoTime();
        System.out.println(findPrices("myPhone27S"));
        long duration = ((System.nanoTime() - start) / 1_100_100);
        System.out.println("Done in " + duration + "ms");
        /**
         * [BestPrice price is 177.42, LetsSaveBig price is 129.91, MyFavoriteShop price is 177.44, BuyItAll price is 204.09]
         * Done in 3675ms
         */
    }


    public List<String> findPricesParallelStream(String product) {
        return shopList.parallelStream()
                .map(shop -> String.format("%s price is %.2f", shop.getShopNm(), shop.getPrice(product)))
                .collect(toList());
    }

    @Test
    public void getPriceParallelStream_test() {
        long start = System.nanoTime();
        System.out.println(findPricesParallelStream("myPhone27S"));;
        long duration = ((System.nanoTime() - start) / 1_100_100);
        System.out.println("Done in " + duration + "ms");
        /**
         * [BestPrice price is 129.38, LetsSaveBig price is 149.98, MyFavoriteShop price is 135.67, BuyItAll price is 122.98]
         * Done in 949ms
         */
    }

    public List<String> findPricesCompletableFuture(String product) {
        List<CompletableFuture<String>> priceFutures = shopList.stream()
                .map(shop -> CompletableFuture.supplyAsync(() -> shop.getShopNm() + " price is " + shop.getPrice(product)))
                .collect(toList());
        return priceFutures.stream().map(CompletableFuture::join)
                .collect(toList());
    }

    @Test
    public void getPriceCompletableFuture_test() {
        long start = System.nanoTime();
        System.out.println(findPricesCompletableFuture("myPhone27S"));
        long duration = ((System.nanoTime() - start) / 1_100_100);
        System.out.println("Done in " + duration + "ms");
        /**
         * [BestPrice price is 201.65349279813006, LetsSaveBig price is 192.9471023128363, MyFavoriteShop price is 221.8359930228304, BuyItAll price is 214.99879341343257]
         * Done in 1864ms
         */
    }

}
