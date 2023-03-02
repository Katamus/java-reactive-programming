package com.learnreactiveprogramming.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

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
}
