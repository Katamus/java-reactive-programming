package com.learnreactiveprogramming;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

public class ColdAndHotPublisherTest {


    @Test
    void coldPlubliserTest(){
        var flux = Flux.range(1,10);
        flux.subscribe(x-> System.out.println("Subscriber 1 : " + x));
        flux.subscribe(y-> System.out.println("Subscriber 2 : " + y));
    }

}
