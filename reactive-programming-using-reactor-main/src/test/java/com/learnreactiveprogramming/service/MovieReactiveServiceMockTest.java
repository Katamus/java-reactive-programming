package com.learnreactiveprogramming.service;

import com.learnreactiveprogramming.domain.Movie;
import com.learnreactiveprogramming.domain.Review;
import com.learnreactiveprogramming.exception.MovieException;
import com.learnreactiveprogramming.exception.NetworkException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MovieReactiveServiceMockTest {

    @Mock
    private MovieInfoService movieInfoService;


    @Mock
    private ReviewService reviewService;

    @InjectMocks
    MovieReactiveService reactiveMovieService;

    @InjectMocks
    MovieReactiveService movieReactiveService;

    @Test
    void getAllMovies(){

        Mockito.when(movieInfoService.retrieveMoviesFlux()).thenCallRealMethod();

        Mockito.when(reviewService.retrieveReviewsFlux(anyLong()))
                .thenCallRealMethod();

        var moviesFlux = reactiveMovieService.getAllMovies();

        StepVerifier.create(moviesFlux).expectNextCount(3).verifyComplete();


    }

    @Test
    void getAllMovies_1(){

        var erroMessage = "Exception occurred in ReviewService";

        Mockito.when(movieInfoService.retrieveMoviesFlux()).thenCallRealMethod();

        Mockito.when(reviewService.retrieveReviewsFlux(anyLong())).thenThrow(new RuntimeException(erroMessage));

        var moviesFlux = reactiveMovieService.getAllMovies();

        StepVerifier.create(moviesFlux)
                //.expectError(MovieException.class)
                .expectErrorMessage(erroMessage)
                .verify();
    }

    @Test
    void getAllMovies_repeat(){

        var erroMessage = "Exception occurred in ReviewService";

        Mockito.when(movieInfoService.retrieveMoviesFlux()).thenCallRealMethod();

        Mockito.when(reviewService.retrieveReviewsFlux(anyLong())).thenCallRealMethod();

        var moviesFlux = reactiveMovieService.getAllMovies_repeat();

        StepVerifier.create(moviesFlux)
                .expectNextCount(6)
                .thenCancel()
                .verify();

        verify(reviewService,times(6))
                .retrieveReviewsFlux(isA(Long.class));
    }

    @Test
    void getAllMovies_repeat_n(){

        var erroMessage = "Exception occurred in ReviewService";

        Mockito.when(movieInfoService.retrieveMoviesFlux()).thenCallRealMethod();

        Mockito.when(reviewService.retrieveReviewsFlux(anyLong())).thenCallRealMethod();

        var noOfTimes = 2L;

        var moviesFlux = reactiveMovieService.getAllMovies_repeat_n(noOfTimes);

        StepVerifier.create(moviesFlux)
                .expectNextCount(9)
                .verifyComplete();

        verify(reviewService,times(6))
                .retrieveReviewsFlux(isA(Long.class));
    }

    @Test
    void getAllMovies_retry(){

        var erroMessage = "Exception occurred in ReviewService";

        Mockito.when(movieInfoService.retrieveMoviesFlux()).thenCallRealMethod();

        Mockito.when(reviewService.retrieveReviewsFlux(anyLong())).thenThrow(new RuntimeException(erroMessage));

        var moviesFlux = reactiveMovieService.getAllMovies_retryWhen();

        StepVerifier.create(moviesFlux)
                //.expectError(MovieException.class)
                .expectErrorMessage(erroMessage)
                .verify();

        verify(reviewService,times(4))
                .retrieveReviewsFlux(isA(Long.class));
    }

    @Test
    void getAllMovies_retryWhen(){

        var erroMessage = "Exception occurred in ReviewService";

        Mockito.when(movieInfoService.retrieveMoviesFlux()).thenCallRealMethod();

        Mockito.when(reviewService.retrieveReviewsFlux(anyLong())).thenThrow(new NetworkException(erroMessage));

        var moviesFlux = reactiveMovieService.getAllMovies_retryWhen();

        StepVerifier.create(moviesFlux)
                //.expectError(MovieException.class)
                .expectErrorMessage(erroMessage)
                .verify();

        verify(reviewService,times(4))
                .retrieveReviewsFlux(isA(Long.class));
    }




}