package com.learnreactiveprogramming.service;

import com.learnreactiveprogramming.domain.Movie;
import com.learnreactiveprogramming.domain.Review;
import com.learnreactiveprogramming.exception.MovieException;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
public class MovieReactiveService {

    private MovieInfoService movieInfoService;
    private ReviewService reviewService;

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

    public Flux<Movie> getAllMovies_retry(){

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
                .retry(3)
                .log();
    }

    public Mono<Movie> getMovieById(long movieId){

        var moviesInfoMono = movieInfoService.retrieveMovieInfoMonoUsingId(movieId);
        var reviewsFlux = reviewService.retrieveReviewsFlux(movieId).collectList();
        return moviesInfoMono.zipWith(reviewsFlux,(movieInfo,reviews)-> new Movie(movieInfo,reviews));
    }

    public Mono<Movie> getMovieById_flatMap(long movieId){

        return  movieInfoService
                .retrieveMovieInfoMonoUsingId(movieId)
                .flatMap(movieInfo -> {
                    Mono<List<Review>> reviews = reviewService.retrieveReviewsFlux(movieId).collectList();
                    return reviews.map(reviews1 -> new Movie(movieInfo,reviews1));
                });

    }
}
