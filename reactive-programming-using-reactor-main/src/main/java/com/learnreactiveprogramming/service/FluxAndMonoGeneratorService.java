package com.learnreactiveprogramming.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Random;

public class FluxAndMonoGeneratorService {

    public Flux<String> namesFlux(){
        return Flux.fromIterable(List.of("alex","ben","chloe")).log(); // db or a remote service call
    }
    public Flux<String> namesFlux_map(int StringLength){
        // filter the String whose length is greater than 3
        return Flux.fromIterable(List.of("alex","ben","chloe"))
                .map(String::toUpperCase)
                .filter(s->s.length() > StringLength)
                .map( s-> s.length() + "-"+s)
                .log(); // db or a remote service call
    }

    public Flux<String> namesFlux_inmutability(){
        var namesFlux = Flux.fromIterable(List.of("alex","ben","chloe"));
        namesFlux.map(String::toUpperCase).log(); // db or a remote service call
        return namesFlux;
    }

    public Mono<String> nameMono(){
        return Mono.just("alex");
    }

    public Mono<String> nameMono_map_filter(int stringLength){
        return Mono.just("alex")
                .filter(s->s.length() > stringLength)
                .map(s->s.toUpperCase());
    }

    public static void main(String[] args) {
        FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();
        fluxAndMonoGeneratorService.namesFlux()
                .subscribe(name -> {
                    System.out.println("Name is : " + name);
                });

        fluxAndMonoGeneratorService.nameMono()
                .subscribe(name->{
                    System.out.println("Mono name is : + name");
                });
    }

    public Flux<String> namesFlux_flatmap(int StringLength){
        // filter the String whose length is greater than 3
        return Flux.fromIterable(List.of("alex","ben","chloe"))
                .map(String::toUpperCase)
                .filter(s->s.length() > StringLength)
                .flatMap(s->splitString(s))
                .log(); // db or a remote service call
    }

    public Flux<String> namesFlux_flatmap_async(int StringLength){
        // filter the String whose length is greater than 3
        return Flux.fromIterable(List.of("alex","ben","chloe"))
                .map(String::toUpperCase)
                .filter(s->s.length() > StringLength)
                .flatMap(s->splitString_withDelay(s))
                .log(); // db or a remote service call
    }

    public Flux<String> namesFlux_concatmap(int StringLength){
        // filter the String whose length is greater than 3
        return Flux.fromIterable(List.of("alex","ben","chloe"))
                .map(String::toUpperCase)
                .filter(s->s.length() > StringLength)
                .concatMap(s->splitString_withDelay(s))
                .log(); // db or a remote service call
    }

    public Flux<String> splitString(String name){
        var charArray = name.split("");
        return Flux.fromArray(charArray);
    }
    public Flux<String> splitString_withDelay(String name){
        var charArray = name.split("");
        //var delay = new Random().nextInt(1000);
        var delay = 1000;
        return Flux.fromArray(charArray).delayElements(Duration.ofMillis(delay));
    }
}
