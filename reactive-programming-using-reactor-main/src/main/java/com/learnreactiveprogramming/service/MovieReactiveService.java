package com.learnreactiveprogramming.service;

import com.learnreactiveprogramming.domain.Movie;
import com.learnreactiveprogramming.domain.Review;
import com.learnreactiveprogramming.exception.MovieException;
import com.learnreactiveprogramming.exception.NetworkException;
import com.learnreactiveprogramming.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;
import reactor.util.retry.RetryBackoffSpec;

import java.time.Duration;
import java.util.List;

@Slf4j
public class MovieReactiveService {

    private MovieInfoService movieInfoService;
    private ReviewService reviewService;

    private RevenueService revenueService;

    public MovieReactiveService(MovieInfoService movieInfoService, ReviewService reviewService, RevenueService revenueService) {
        this.movieInfoService = movieInfoService;
        this.reviewService = reviewService;
        this.revenueService = revenueService;
    }

    public MovieReactiveService(MovieInfoService movieInfoService, ReviewService reviewService) {
        this.movieInfoService = movieInfoService;
        this.reviewService = reviewService;
    }

    public Flux<Movie> getAllMovies(){

        var moviesInfoFlux = movieInfoService.retrieveMoviesFlux();
        return moviesInfoFlux
                .flatMap(movieInfo -> {
                    Mono<List<Review>> reviewsMono = reviewService.retrieveReviewsFlux(movieInfo.getMovieInfoId())
                    .collectList();
                    return reviewsMono.map(reviewsList -> new Movie(movieInfo,reviewsList));
                })
                .onErrorMap((ex)->{
                    log.error("Exception is : ", ex);
                    throw new MovieException(ex.getMessage());
                })
                .log();
    }

    public Flux<Movie> getAllMovies_restClient(){

        var moviesInfoFlux = movieInfoService.retrieveAllMovieInfo_RestClient();
        return moviesInfoFlux
                .flatMap(movieInfo -> {
                    Mono<List<Review>> reviewsMono = reviewService.retrieveReviewsFlux_RestCliente(movieInfo.getMovieInfoId())
                            .collectList();
                    return reviewsMono.map(reviewsList -> new Movie(movieInfo,reviewsList));
                })
                .onErrorMap((ex)->{
                    log.error("Exception is : ", ex);
                    throw new MovieException(ex.getMessage());
                })
                .log();
    }

    public Mono<Movie> getMovieById(long movieId){

        var moviesInfoMono = movieInfoService.retrieveMovieInfoMonoUsingId(movieId);
        var reviewsFlux = reviewService.retrieveReviewsFlux(movieId).collectList();
        return moviesInfoMono.zipWith(reviewsFlux,(movieInfo,reviews)-> new Movie(movieInfo,reviews));
    }

    public Mono<Movie> getMovieById_withRevenenue(long movieId){

        var moviesInfoMono = movieInfoService.retrieveMovieInfoMonoUsingId(movieId);
        var reviewsFlux = reviewService.retrieveReviewsFlux(movieId).collectList();

        var revenueMono =  Mono.fromCallable(() -> revenueService.getRevenue(movieId))
                .subscribeOn(Schedulers.boundedElastic());
        ;

        return moviesInfoMono.zipWith(reviewsFlux,(movieInfo,reviews)-> new Movie(movieInfo,reviews))
                .zipWith(revenueMono,(movie, revenue) -> {
                    movie.setRevenue(revenue);
                    return movie;
                });
    }

    public Flux<Movie> getAllMovies_retryWhen(){

        var retryWhen = Retry.fixedDelay(3 , Duration.ofMillis(500))
                .filter(throwable -> throwable instanceof MovieException )
                .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) ->
                        Exceptions.propagate(retrySignal.failure()
                        ));

        var moviesInfoFlux = movieInfoService.retrieveMoviesFlux();
        return moviesInfoFlux
                .flatMap(movieInfo -> {
                    Mono<List<Review>> reviewsMono = reviewService.retrieveReviewsFlux(movieInfo.getMovieInfoId())
                            .collectList();
                    return reviewsMono.map(reviewsList -> new Movie(movieInfo,reviewsList));
                })
                .onErrorMap((ex)->{
                    log.error("Exception is : ", ex);
                    if(ex instanceof NetworkException)
                        throw new MovieException(ex.getMessage());
                    else
                        throw new ServiceException(ex.getMessage());
                })
                .retryWhen(retryWhen)
                .log();
    }

    public Flux<Movie> getAllMovies_repeat(){

        var moviesInfoFlux = movieInfoService.retrieveMoviesFlux();
        return moviesInfoFlux
                .flatMap(movieInfo -> {
                    Mono<List<Review>> reviewsMono = reviewService.retrieveReviewsFlux(movieInfo.getMovieInfoId())
                            .collectList();
                    return reviewsMono.map(reviewsList -> new Movie(movieInfo,reviewsList));
                })
                .onErrorMap((ex)->{
                    log.error("Exception is : ", ex);
                    if(ex instanceof NetworkException)
                        throw new MovieException(ex.getMessage());
                    else
                        throw new ServiceException(ex.getMessage());
                })
                .retryWhen(getRetryBackoffSpec())
                .repeat()
                .log();
    }

    public Flux<Movie> getAllMovies_repeat_n(long n){

        var moviesInfoFlux = movieInfoService.retrieveMoviesFlux();
        return moviesInfoFlux
                .flatMap(movieInfo -> {
                    Mono<List<Review>> reviewsMono = reviewService.retrieveReviewsFlux(movieInfo.getMovieInfoId())
                            .collectList();
                    return reviewsMono.map(reviewsList -> new Movie(movieInfo,reviewsList));
                })
                .onErrorMap((ex)->{
                    log.error("Exception is : ", ex);
                    if(ex instanceof NetworkException)
                        throw new MovieException(ex.getMessage());
                    else
                        throw new ServiceException(ex.getMessage());
                })
                .retryWhen(getRetryBackoffSpec())
                .repeat(n)
                .log();
    }


    public Mono<Movie> getMovieById_flatMap(long movieId){

        return  movieInfoService
                .retrieveMovieInfoMonoUsingId(movieId)
                .flatMap(movieInfo -> {
                    Mono<List<Review>> reviews = reviewService.retrieveReviewsFlux(movieId).collectList();
                    return reviews.map(reviews1 -> new Movie(movieInfo,reviews1));
                });

    }


    public RetryBackoffSpec getRetryBackoffSpec(){
        return  Retry.fixedDelay(3 , Duration.ofMillis(500))
                .filter(throwable -> throwable instanceof MovieException )
                .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) ->
                        Exceptions.propagate(retrySignal.failure()
                        ));
    }
}
