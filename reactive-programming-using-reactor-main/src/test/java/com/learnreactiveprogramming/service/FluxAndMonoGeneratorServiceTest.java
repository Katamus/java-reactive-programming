package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.List;

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

    @Test
    void nameMono_map_filter() {
        var nameMono = fluxAndMonoGeneratorService.namesMono_map_filter(3);
        StepVerifier.create(nameMono)
                .expectNext("ALEX")
                .verifyComplete();
    }

    @Test
    void namesFlux_flatmap() {
        int stringLength = 3;
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_flatmap(stringLength);
        StepVerifier.create(namesFlux)
                .expectNext("A","L","E","X","C","H","L","O","E")
                .verifyComplete();
    }

    @Test
    void namesFlux_flatmap_async() {
        int stringLength = 3;
        var namesFlux_flatmap_async = fluxAndMonoGeneratorService.namesFlux_flatmap_async(stringLength);
        StepVerifier.create(namesFlux_flatmap_async)
                .expectNextCount(9)
                .verifyComplete();
    }

    @Test
    void namesFlux_concatmap() {

        int stringLength = 3;
        var namesFlux_concatmap = fluxAndMonoGeneratorService.namesFlux_concatmap(stringLength);
        StepVerifier.create(namesFlux_concatmap)
                .expectNext("A","L","E","X","C","H","L","O","E")
                .verifyComplete();
    }

    @Test
    void nameMono_flatmap_filter() {
        int stringLength = 3;

        var value = fluxAndMonoGeneratorService.nameMono_flatmap_filter(stringLength);

        StepVerifier.create(value)
                .expectNext(List.of("A","L","E","X"))
                .verifyComplete();
    }

    @Test
    void nameMono_flatmapmany_filter() {
        int stringLength = 3;

        var value = fluxAndMonoGeneratorService.nameMono_flatmapmany_filter(stringLength);

        StepVerifier.create(value)
                .expectNext("A","L","E","X")
                .verifyComplete();
    }

    @Test
    void namesFlux_transform() {

        int stringLength = 3;
        var namesFlux_transform = fluxAndMonoGeneratorService.namesFlux_transform(stringLength);
        StepVerifier.create(namesFlux_transform)
                .expectNext("A","L","E","X","C","H","L","O","E")
                .verifyComplete();
    }

    @Test
    void namesFlux_DefaulitEmpty() {

        int stringLength = 7;
        var namesFlux_DefaulitEmpty = fluxAndMonoGeneratorService.namesFlux_DefaulitEmpty(stringLength);
        StepVerifier.create(namesFlux_DefaulitEmpty)
                .expectNext("Default")
                .verifyComplete();
    }

    @Test
    void namesFlux_SwitchitEmpty() {

        int stringLength = 7;
        var namesFlux_SwitchitEmpty = fluxAndMonoGeneratorService.namesFlux_SwitchitEmpty(stringLength);
        StepVerifier.create(namesFlux_SwitchitEmpty)
                .expectNext("D","E","F","A","U","L","T")
                .verifyComplete();
    }


    @Test
    void explore_concat() {
        var explore_concat = fluxAndMonoGeneratorService.explore_concat();
        StepVerifier.create(explore_concat)
                .expectNext("A","B","C","D","E","F")
                .verifyComplete();

    }


    @Test
    void explore_concatWith() {
        var explore_concatWith = fluxAndMonoGeneratorService.explore_concatWith();
        StepVerifier.create(explore_concatWith)
                .expectNext("A","B","C","D","E","F")
                .verifyComplete();
    }

    @Test
    void explore_concatWith_mono() {
        var explore_concatWith_mono = fluxAndMonoGeneratorService.explore_concatWith_mono();
        StepVerifier.create(explore_concatWith_mono)
                .expectNext("A","B")
                .verifyComplete();
    }

    @Test
    void explore_merge() {

        var value = fluxAndMonoGeneratorService.explore_merge();
        StepVerifier.create(value)
                .expectNext("A","D","B","E","C","F")
                .verifyComplete();
    }

    @Test
    void explore_mergeWith() {
        var value = fluxAndMonoGeneratorService.explore_mergeWith();
        StepVerifier.create(value)
                .expectNext("A","D","B","E","C","F")
                .verifyComplete();
    }

    @Test
    void explore_mergetWith_mono() {
        var explore_merge_mono = fluxAndMonoGeneratorService.explore_merge_mono();
        StepVerifier.create(explore_merge_mono)
                .expectNext("A","B")
                .verifyComplete();
    }

    @Test
    void explore_mergeSequential() {

        var value = fluxAndMonoGeneratorService.explore_mergeSequential();
        StepVerifier.create(value)
                .expectNext("A","B","C","D","E","F")
                .verifyComplete();

    }

    @Test
    void explore_zip() {
        var value = fluxAndMonoGeneratorService.explore_zip();
        StepVerifier.create(value)
                .expectNext("AD","BE","CF")
                .verifyComplete();
    }

    @Test
    void explore_zip_1() {
        var value = fluxAndMonoGeneratorService.explore_zip_1();
        StepVerifier.create(value)
                .expectNext("AD14","BE25","CF36")
                .verifyComplete();
    }
}
