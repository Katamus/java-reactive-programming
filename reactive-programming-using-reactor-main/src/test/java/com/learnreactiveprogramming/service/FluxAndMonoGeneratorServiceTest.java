package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

public class FluxAndMonoGeneratorServiceTest {

    FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();

    @Test
    void namesFlux() {

        var namesFlux = fluxAndMonoGeneratorService.namesFlux();

        StepVerifier.create(namesFlux)
                //.expectNext("alex","ben","chloe")
                //.expectNextCount(3)
                .expectNext("alex")
                .expectNextCount(2)
                .verifyComplete();

    }


    @Test
    void nameMono() {
        var nameMono = fluxAndMonoGeneratorService.nameMono();
        StepVerifier.create(nameMono)
                .expectNext("alex")
                .verifyComplete();
    }

    @Test
    void namesFlux_map() {
        var namesFlux_map = fluxAndMonoGeneratorService.namesFlux_map(3);
        StepVerifier.create(namesFlux_map)
                .expectNext("4-ALEX","5-CHLOE")
                .verifyComplete();
    }

    @Test
    void namesFlux_inmutability() {
        var namesFlux_inmutability = fluxAndMonoGeneratorService.namesFlux_inmutability();
        StepVerifier.create(namesFlux_inmutability)
                .expectNext("alex","ben","chloe")
                .verifyComplete();
    }
}
