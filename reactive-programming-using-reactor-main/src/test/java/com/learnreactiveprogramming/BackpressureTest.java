package com.learnreactiveprogramming;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SignalType;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class BackpressureTest {


    @Test
    void testBackPressure(){
        var numbreRange = Flux.range(1,100).log();

        numbreRange
                .subscribe(new BaseSubscriber<Integer>() {
                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        request(2);
                    }
                    @Override
                    protected void hookOnNext(Integer value) {
                        log.info("hookOnNext : {}",value);
                        if(value == 2){
                            cancel();
                        }
                    }
                    @Override
                    protected void hookOnCancel() {
                        log.info("Inside OnCancel");
                    }
                    @Override
                    protected void hookOnComplete() {
                        super.hookOnComplete();
                    }
                    @Override
                    protected void hookOnError(Throwable throwable) {
                        super.hookOnError(throwable);
                    }
                    @Override
                    protected void hookFinally(SignalType type) {
                        super.hookFinally(type);
                    }
                });
            //.subscribe(num -> {
            //    log.info("Number is : {} ", num);
            //});

    }

    @Test
    void testBackPressure_1() throws InterruptedException {
        var numbreRange = Flux.range(1,100).log();
        CountDownLatch latch = new CountDownLatch(1);
        numbreRange
                .subscribe(new BaseSubscriber<Integer>() {
                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        request(2);
                    }
                    @Override
                    protected void hookOnNext(Integer value) {
                        log.info("hookOnNext : {}",value);
                        if(value % 2 == 0 && value < 50){
                           request(2);
                        }else{
                            cancel();
                        }
                    }
                    @Override
                    protected void hookOnCancel() {
                        log.info("Inside OnCancel");
                        latch.countDown();
                    }
                    @Override
                    protected void hookOnComplete() {
                        super.hookOnComplete();
                    }
                    @Override
                    protected void hookOnError(Throwable throwable) {
                        super.hookOnError(throwable);
                    }

                    @Override
                    protected void hookFinally(SignalType type) {
                        super.hookFinally(type);
                    }
                });

        assertTrue(latch.await(5L, TimeUnit.SECONDS));
    }


    @Test
    void testBackPressure_drop() throws InterruptedException {
        var numbreRange = Flux.range(1,100).log();
        CountDownLatch latch = new CountDownLatch(1);

        numbreRange.onBackpressureDrop(item -> {
            log.info("Dropped Items are : {}", item);
        }).subscribe(new BaseSubscriber<Integer>() {
                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        request(2);
                    }
                    @Override
                    protected void hookOnNext(Integer value) {
                        log.info("hookOnNext : {}",value);
                        if(value == 2 ){
                            hookOnCancel();
                        }
                    }
                    @Override
                    protected void hookOnCancel() {
                        log.info("Inside OnCancel");
                        latch.countDown();
                    }
                    @Override
                    protected void hookOnComplete() {
                        super.hookOnComplete();
                    }
                    @Override
                    protected void hookOnError(Throwable throwable) {
                        super.hookOnError(throwable);
                    }

                    @Override
                    protected void hookFinally(SignalType type) {
                        super.hookFinally(type);
                    }
                });

        assertTrue(latch.await(5L, TimeUnit.SECONDS));
    }

    @Test
    void testBackPressure_buffer() throws InterruptedException {
        var numbreRange = Flux.range(1,100).log();
        CountDownLatch latch = new CountDownLatch(1);

        numbreRange.onBackpressureBuffer(10, i->{
            log.info("Last Buffered element is : {} ", 1);
        }).subscribe(new BaseSubscriber<Integer>() {
            @Override
            protected void hookOnSubscribe(Subscription subscription) {
                request(1);
            }
            @Override
            protected void hookOnNext(Integer value) {
                log.info("hookOnNext : {}",value);
                if(value < 50 ){
                    request(1);
                }else{
                    hookOnCancel();
                }
            }
            @Override
            protected void hookOnCancel() {
                log.info("Inside OnCancel");
                latch.countDown();
            }
            @Override
            protected void hookOnComplete() {
                super.hookOnComplete();
            }
            @Override
            protected void hookOnError(Throwable throwable) {
                super.hookOnError(throwable);
            }

            @Override
            protected void hookFinally(SignalType type) {
                super.hookFinally(type);
            }
        });

        assertTrue(latch.await(5L, TimeUnit.SECONDS));
    }
}
