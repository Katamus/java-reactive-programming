package com.learnreactiveprogramming;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SignalType;

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
                    protected Subscription upstream() {
                        return super.upstream();
                    }

                    @Override
                    public boolean isDisposed() {
                        return super.isDisposed();
                    }

                    @Override
                    public void dispose() {
                        super.dispose();
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

                    @Override
                    public String toString() {
                        return super.toString();
                    }
                });
            //.subscribe(num -> {
            //    log.info("Number is : {} ", num);
            //});

    }
}
