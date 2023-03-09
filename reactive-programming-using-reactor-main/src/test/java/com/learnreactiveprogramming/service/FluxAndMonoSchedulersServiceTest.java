package com.learnreactiveprogramming.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class FluxAndMonoSchedulersServiceTest {

    FluxAndMonoSchedulersService fluxAndMonoSchedulersService = new FluxAndMonoSchedulersService();

    @Test
    void explore_publishOn() {

        var flux = fluxAndMonoSchedulersService.explore_publishOn();

        StepVerifier.create(flux)
                .expectNextCount(6)
                .verifyComplete();

    }

    @Test
    void explore_subscribeOn() {
        var flux = fluxAndMonoSchedulersService.explore_subscribeOn();
        StepVerifier.create(flux)
                .expectNextCount(6)
                .verifyComplete();
    }

    @Test
    void explore_parallel() {

        var flux = fluxAndMonoSchedulersService.explore_parallel();

        var noOfCores = Runtime.getRuntime().availableProcessors();

        log.info("noOfCores : {} ",noOfCores);

        StepVerifier.create(flux)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void explore_parallel_usingFlatmap() {

        var flux = fluxAndMonoSchedulersService.explore_parallel_usingFlatmap();
        StepVerifier.create(flux)
                .expectNextCount(3)
                .verifyComplete();

    }



    @Test
    void explore_parallel_usingFlatmap_1() {

        var flux = fluxAndMonoSchedulersService.explore_parallel_usingFlatmap_1();
        StepVerifier.create(flux)
                .expectNextCount(6)
                .verifyComplete();

    }



}