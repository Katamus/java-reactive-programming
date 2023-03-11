package com.learnreactiveprogramming;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.time.Duration;

import static com.learnreactiveprogramming.util.CommonUtil.delay;

public class ColdAndHotPublisherTest {


    @Test
    void coldPlubliserTest(){
        var flux = Flux.range(1,10);
        flux.subscribe(x-> System.out.println("Subscriber 1 : " + x));
        flux.subscribe(y-> System.out.println("Subscriber 2 : " + y));
    }

    @Test
    void gotPlubliserTest(){
        var flux = Flux.range(1,10)
                .delayElements(Duration.ofSeconds(1));

        ConnectableFlux<Integer> connectableFlux = flux.publish();
        connectableFlux.connect();

        connectableFlux.subscribe(x-> System.out.println("Subscriber 1 : " + x));
        delay(4000);
        connectableFlux.subscribe(x-> System.out.println("Subscriber 2 : " + x));
        delay(10000);
    }

    @Test
    void gotPlubliserTest_autoConnect(){
        var flux = Flux.range(1,10)
                .delayElements(Duration.ofSeconds(1));

        var hotSource = flux.publish().autoConnect(2);

        hotSource.subscribe(x-> System.out.println("Subscriber 1 : " + x));
        delay(2000);
        hotSource.subscribe(x-> System.out.println("Subscriber 2 : " + x));
        delay(2000);
        hotSource.subscribe(x-> System.out.println("Subscriber 3 : " + x));
        delay(10000);
    }

    @Test
    void gotPlubliserTest_refCount(){
        var flux = Flux.range(1,10)
                .delayElements(Duration.ofSeconds(1))
                .doOnCancel(()->{
                    System.out.println("Received Cancel Signal");
                });

        var hotSource = flux.publish().refCount(2);

        var disposable = hotSource.subscribe(x-> System.out.println("Subscriber 1 : " + x));
        delay(2000);
        var disposable1 = hotSource.subscribe(x-> System.out.println("Subscriber 2 : " + x));
        delay(2000);

        disposable.dispose();
        disposable1.dispose();
        hotSource.subscribe(x-> System.out.println("Subscriber 3 : " + x));
        delay(2000);

        hotSource.subscribe(x-> System.out.println("Subscriber 4 : " + x));
        delay(10000);
    }
}
