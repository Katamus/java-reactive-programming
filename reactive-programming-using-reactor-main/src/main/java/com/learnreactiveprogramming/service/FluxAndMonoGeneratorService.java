package com.learnreactiveprogramming.service;

import com.learnreactiveprogramming.exception.ReactorException;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

@Slf4j
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
                .doOnNext(s -> {
                    System.out.println(s);
                })
                .doOnSubscribe(s->{
                    System.out.println("Subscription is : "+s);
                })
                .doOnComplete(() -> {
                    System.out.println("Inside the complete callback");
                })
                .doFinally(signalType -> {
                    System.out.println("inside dofinally : "+ signalType);
                })
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

    public Flux<String> explore_mergeSequential(){

        var abcFlux = Flux.just("A","B","C")
                .delayElements(Duration.ofMillis(100));

        var defFlux = Flux.just("D","E","F")
                .delayElements(Duration.ofMillis(125));

        return Flux.mergeSequential(abcFlux,defFlux).log();

    }

    public Flux<String> explore_zip(){

        var abcFlux = Flux.just("A","B","C");

        var defFlux = Flux.just("D","E","F");

        return Flux.zip(abcFlux,defFlux,(first, second) -> first+second).log();

    }

    public Flux<String> explore_zip_1(){

        var abcFlux = Flux.just("A","B","C");

        var defFlux = Flux.just("D","E","F");

        var _123defFlux = Flux.just("1","2","3");

        var _456defFlux = Flux.just("4","5","6");

        return Flux.zip(abcFlux,defFlux,_123defFlux,_456defFlux)
                .map(t4 -> t4.getT1()+t4.getT2()+t4.getT3()+t4.getT4())
                .log();

    }

    public Flux<String> explore_zipWith(){

        var abcFlux = Flux.just("A","B","C");

        var defFlux = Flux.just("D","E","F");

        return abcFlux.zipWith(defFlux,(first, second) -> first+second).log();

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

    public Mono<String> explore_zipWith_mono(){

        var aMono = Mono.just("A");

        var bMono = Mono.just("B");

        return aMono.zipWith(bMono)
                .map(t2->t2.getT1()+t2.getT2())
                .log();

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

    public Flux<String> exception_flux(){

        return Flux.just("A","B","C")
                .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
                .concatWith(Flux.just("D"));
    }

    public Flux<String> explore_OnErrorReturn(){

        return Flux.just("A","B","C")
                .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
                .onErrorReturn("D")
                .log();
    }

    public Flux<String> explore_OnErrorResume(Exception e){

        var recoveryFlux = Flux.just("D","E","F");

        return Flux.just("A","B","C")
                .concatWith(Flux.error(e))
                .onErrorResume(ex -> {
                    if(e instanceof IllegalAccessException){
                        log.error("Exception is ", ex);
                        return recoveryFlux;
                    }else{
                        return Flux.error(e);
                    }

                })
                .log();
    }

    public Flux<String> explore_OnErrorContinue(){

        var recoveryFlux = Flux.just("D","E","F");

        return Flux.just("A","B","C")
                .map(name-> {
                    if(name.equals("B"))
                        throw new IllegalStateException("Exception Occurred");

                    return name;
                })
                .concatWith(Flux.just("D"))
                .onErrorContinue((ex, name) -> {
                    log.error("Exception is ", ex);
                    log.info("name is {}",name);
                })
                .log();
    }

    public Flux<String> explore_OnErrorMap(){

        var recoveryFlux = Flux.just("D","E","F");

        return Flux.just("A","B","C")
                .map(name-> {
                    if(name.equals("B"))
                        throw new IllegalStateException("Exception Occurred");

                    return name;
                })
                .concatWith(Flux.just("D"))
                .onErrorMap(ex -> {
                    log.error("Exception is", ex);
                    return new ReactorException(ex,ex.getMessage());
                })
                .log();
    }

    public Flux<String> explore_doOnError(){
        return Flux.just("A","B","C")
                .concatWith(Flux.error(new IllegalStateException("Exception Ocurred")))
                .doOnError(ex->{
                    log.error("Exception is ",ex);
                }).log();
    }

    public Mono<Object> explore_Mono_OnErrorReturn(){
        return Mono.just("A")
                .map(value -> {
                    throw new RuntimeException("Exception Occurred");
                }).onErrorReturn("abc").log();
    }

    public Mono<Object> exception_mono_onErrorMap(Exception ex){
        return  Mono.just("B")
                .map(s -> {
                    throw new RuntimeException("Exception Occurred");
                })
                .onErrorMap(throwable -> {
                    return new ReactorException(throwable,"Exception Occurred 2");
                });
    }

    public Mono<String> exception_mono_onErrorContinue(String value){
        return Mono.just(value)
                .map(s -> {
                    if("abc".equals(value)){
                        throw new RuntimeException("Exception Occurred");
                    }
                    return value;
                })
                .onErrorContinue((throwable, o) -> {
                    log.error("Exception :"+throwable);
                    log.error("input :"+o.toString());
                }).log();
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
