package com.learnreactiveprogramming.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

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

    public Flux<String> namesFlux_transform(int StringLength){
        Function<Flux<String>,Flux<String>> filterMap = stringFlux -> stringFlux
                    .filter(s->s.length() > StringLength)
                .map(String::toUpperCase);

        // filter the String whose length is greater than 3
        return Flux.fromIterable(List.of("alex","ben","chloe"))
                .transform(filterMap)
                .flatMap(s -> splitString(s))
                .log(); // db or a remote service call
    }

    public Flux<String> namesFlux_DefaulitEmpty(int StringLength){

        Function<Flux<String>,Flux<String>> filterMap = stringFlux -> stringFlux
                .map(String::toUpperCase)
                .filter(s->s.length() > StringLength);

        // filter the String whose length is greater than 3
        return Flux.fromIterable(List.of("alex","ben","chloe"))
                .transform(filterMap)
                .defaultIfEmpty("Default")
                .log(); // db or a remote service call
    }

    public Flux<String> namesFlux_SwitchitEmpty(int StringLength){

        Function<Flux<String>,Flux<String>> filterMap = stringFlux -> stringFlux
                .map(String::toUpperCase)
                .filter(s->s.length() > StringLength);

        Flux<String> switchFlux = Flux.just("DEFAULT");

        // filter the String whose length is greater than 3
        return Flux.fromIterable(List.of("alex","ben","chloe"))
                .transform(filterMap)
                .switchIfEmpty(switchFlux)
                .flatMap(s -> splitString(s))
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

    public Mono<String> namesMono_map_filter(int stringLength){
        return Mono.just("alex")
                .filter(s->s.length() > stringLength)
                .defaultIfEmpty("Default")
                .map(s->s.toUpperCase());
    }

    public Mono<String> namesMono_map_filter_switchIfEmpty (int stringLength){
        Mono<String> swichtMono = Mono.just("Default");
        return Mono.just("alex")
                .filter(s->s.length() > stringLength)
                .switchIfEmpty(swichtMono)
                .map(s->s.toUpperCase());
    }

    public Flux<String> explore_concat(){

        var abcFlux = Flux.just("A","B","C");

        var defFlux = Flux.just("D","E","F");

        return Flux.concat(abcFlux,defFlux);

    }

    public Flux<String> explore_merge(){

        var abcFlux = Flux.just("A","B","C")
                .delayElements(Duration.ofMillis(100));

        var defFlux = Flux.just("D","E","F")
                .delayElements(Duration.ofMillis(125));

        return Flux.merge(abcFlux,defFlux).log();

    }

    public Flux<String> explore_mergeWith(){

        var abcFlux = Flux.just("A","B","C")
                .delayElements(Duration.ofMillis(100));

        var defFlux = Flux.just("D","E","F")
                .delayElements(Duration.ofMillis(125));

        return abcFlux.mergeWith(defFlux).log();

    }

    public Flux<String> explore_concatWith(){

        var abcFlux = Flux.just("A","B","C");

        var defFlux = Flux.just("D","E","F");

        return abcFlux.concatWith(defFlux);

    }

    public Flux<String> explore_concatWith_mono(){

        var aMono = Mono.just("A");

        var bMono = Mono.just("B");

        return aMono.concatWith(bMono);

    }

    public Flux<String> explore_merge_mono(){

        var aMono = Mono.just("A");

        var bMono = Mono.just("B");

        return aMono.mergeWith(bMono);

    }

    public Mono<List<String>> nameMono_flatmap_filter(int stringLength){
        return Mono.just("alex")
                .filter(s->s.length() > stringLength)
                .map(s->s.toUpperCase())
                .flatMap(this::splitStringMono)
                .log();
    }

    public Flux<String> nameMono_flatmapmany_filter(int stringLength){
        return Mono.just("alex")
                .filter(s->s.length() > stringLength)
                .map(s->s.toUpperCase())
                .flatMapMany(this::splitString)
                .log();
    }

    private Mono<List<String>> splitStringMono(String s) {
        var charArray = s.split("");
        var charList = List.of(charArray);
        return Mono.just(charList);
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
