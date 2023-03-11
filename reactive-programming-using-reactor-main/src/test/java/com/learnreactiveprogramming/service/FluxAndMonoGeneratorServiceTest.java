package com.learnreactiveprogramming.service;

import com.learnreactiveprogramming.exception.ReactorException;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

import java.time.Duration;
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
    void namesFlux_concatmap_virtualTimer() {

        VirtualTimeScheduler.getOrSet();
        int stringLength = 3;
        var namesFlux_concatmap = fluxAndMonoGeneratorService.namesFlux_concatmap(stringLength);

        StepVerifier.withVirtualTime(() -> namesFlux_concatmap)
                .thenAwait(Duration.ofSeconds(10))
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

    @Test
    void explore_zipWith() {
        var value = fluxAndMonoGeneratorService.explore_zipWith();
        StepVerifier.create(value)
                .expectNext("AD","BE","CF")
                .verifyComplete();
    }

    @Test
    void explore_zipWith_mono() {

        var value = fluxAndMonoGeneratorService.explore_zipWith_mono();
        StepVerifier.create(value)
                .expectNext("AB")
                .verifyComplete();
    }

    @Test
    void exception_flux() {

        var value = fluxAndMonoGeneratorService.exception_flux();

        StepVerifier.create(value)
                .expectNext("A","B","C")
                .expectError(RuntimeException.class)
                .verify();

    }

    @Test
    void exception_flux_2() {

        var value = fluxAndMonoGeneratorService.exception_flux();

        StepVerifier.create(value)
                .expectNext("A","B","C")
                .expectErrorMessage("Exception Occurred")
                .verify();

    }

    @Test
    void explore_OnErrorReturn() {

        var value = fluxAndMonoGeneratorService.explore_OnErrorReturn();

        StepVerifier.create(value)
                .expectNext("A","B","C","D")
                .verifyComplete();

    }

    @Test
    void explore_OnErrorResume(){

        var e = new IllegalStateException("Not a valid State");

        var value = fluxAndMonoGeneratorService.explore_OnErrorResume(e);

        StepVerifier.create(value)
                .expectNext("A","B","C","D","E","F")
                .verifyComplete();


    }

    @Test
    void explore_OnErrorResume_1(){

        var e = new RuntimeException("Not a valid State");

        var value = fluxAndMonoGeneratorService.explore_OnErrorResume(e);

        StepVerifier.create(value)
                .expectNext("A","B","C")
                .expectError(RuntimeException.class)
                .verify();


    }

    @Test
    void explore_OnErrorContinue(){
        var value = fluxAndMonoGeneratorService.explore_OnErrorContinue();
        StepVerifier
                .create(value)
                .expectNext("A","C","D")
                .verifyComplete();
    }

    @Test
    void explore_OnErrorMap(){
        var value = fluxAndMonoGeneratorService.explore_OnErrorMap();
        StepVerifier.create(value)
                .expectNext("A")
                .expectError(ReactorException.class)
                .verify();
    }

    @Test
    void explore_doOnError(){

        var value = fluxAndMonoGeneratorService.explore_doOnError();

        StepVerifier.create(value)
                .expectNext("A","B","C")
                .expectError(IllegalStateException.class)
                .verify();

    }

    @Test
    void  explore_Mono_OnErrorReturn(){
        var value = fluxAndMonoGeneratorService.explore_Mono_OnErrorReturn();

        StepVerifier.create(value)
                .expectNext("abc")
                .verifyComplete();

    }

    @Test
    void exception_mono_onErrorContinue_abc(){
        var value = fluxAndMonoGeneratorService.exception_mono_onErrorContinue("abc");
        StepVerifier.create(value)
                .verifyComplete();

    }

    @Test
    void exception_mono_onErrorContinue_reactor(){
        String input = "reactor";
        var value = fluxAndMonoGeneratorService.exception_mono_onErrorContinue(input);
        StepVerifier.create(value)
                .expectNext(input)
                .verifyComplete();

    }


    @Test
    void explore_generate() {

        var value = fluxAndMonoGeneratorService.explore_generate();

        StepVerifier.create(value)
                .expectNextCount(10)
                .verifyComplete();


    }

    @Test
    void explore_create() {

        var value = fluxAndMonoGeneratorService.explore_create().log();

        StepVerifier.create(value)
                .expectNextCount(6)
                .verifyComplete();

    }
}
