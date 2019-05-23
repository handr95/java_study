import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import java.util.function.Supplier;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Test {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @org.junit.Test
    public void furture_test() {
        ExecutorService executor = Executors.newCachedThreadPool();
        Future<Double> furture = executor.submit(new Callable<Double>(){
            public Double call() {
                return doSomeLongComputation(); //
            }
        });

        doSomethingElse();

        try {
            Double result = furture.get(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            // 현재 스레드에서 대기 중 인터럽트 발생
        } catch (ExecutionException e) {
            // 계산 중 예외 발생
        } catch (TimeoutException e) {
            e.printStackTrace();    //Future가 완료되기 전에 타임아웃 발생
        }

        /*try {
            Double result = furture.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }*/

        doSomethingElse2();

    }

    private void doSomethingElse() {
        try {
            Thread.sleep(100L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("doSomethingElse");
    }

    private void doSomethingElse2() {
        System.out.println("doSomethingElse2");
    }

    public Double doSomeLongComputation(){
        try {
            Thread.sleep(10000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("doSomeLongComputation");
        return null;
    }


    public Integer getTestCode () {
        logger.info("test: testsCode");
        try {
            Thread.sleep(1000L);
            throw new RuntimeException("exception test");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        return  0;
    }


    @org.junit.Test
    public void test01(){
        CompletableFuture<Integer> future = new CompletableFuture<>();
        future.complete(getTestCode());
        /*new Thread(()-> {

        });*/

        //future.complete(getTestCode());

        logger.info("test: start");

        try {
            System.out.println(future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        logger.info("test: end");
    }

    @org.junit.Test
    public void test1() {
        CompletableFuture.runAsync(() -> {
            logger.info("runAsync");
        });

        logger.info("exit");
    }

    @org.junit.Test
    public void test2() {
        Supplier<String> A = () -> {
            try {
                System.out.println("A 스레드 작업 시작");
                Thread.sleep(2000);
                System.out.println("A 스레드 작업 완료");
                return "A 실행";
            } catch (InterruptedException e){
                e.printStackTrace();
                return "실패";
            }
        };

        Function<String, String> B = (aResult) -> {
            try {
                System.out.println("B 스레드 작업 시작");
                Thread.sleep(1000);
                System.out.println("B 스레드 작업 완료");
                return aResult + " B 실행";
            } catch (InterruptedException e){
                e.printStackTrace();
                return "실패";
            }
        };

        Future<String> result = CompletableFuture.
            supplyAsync(A).
            thenApply(aResult -> aResult + " A 성공 -> ").
            thenCompose(aSucceedResult -> CompletableFuture.supplyAsync(() -> B.apply(aSucceedResult)));

        try {
            System.out.println(result.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @org.junit.Test
    public void test3(){
        Supplier<String> A = () -> {
            try {
                System.out.println("A 스레드 작업 시작");
                Thread.sleep(2000);
                System.out.println("A 스레드 작업 완료");
                return "A 실행";
            } catch (InterruptedException e){
                e.printStackTrace();
                return "실패";
            }
        };

        Supplier<String> C = () -> {
            try {
                System.out.println("C 스레드 작업 시작");
                Thread.sleep(500);
                System.out.println("C 스레드 작업 완료");
                return "C 실행";
            } catch (InterruptedException e){
                e.printStackTrace();
                return "실패";
            }
        };

        Future<String> result = CompletableFuture.
            supplyAsync(A).
            thenApply(aResult -> aResult + " A 성공 -> ").
            thenCombine(CompletableFuture.supplyAsync(C), (a, c) -> a + c);

        try {
            System.out.println(result.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @org.junit.Test
    public void test(){
        int cores = Runtime.getRuntime().availableProcessors();
        System.out.println(cores);
    }
}
