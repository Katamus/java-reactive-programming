package com.learnreactiveprogramming.service;

import com.learnreactiveprogramming.exception.MovieException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;

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
    void getAllMovies_retry(){

        var erroMessage = "Exception occurred in ReviewService";

        Mockito.when(movieInfoService.retrieveMoviesFlux()).thenCallRealMethod();

        Mockito.when(reviewService.retrieveReviewsFlux(anyLong())).thenThrow(new RuntimeException(erroMessage));

        var moviesFlux = reactiveMovieService.getAllMovies_retry();

        StepVerifier.create(moviesFlux)
                //.expectError(MovieException.class)
                .expectErrorMessage(erroMessage)
                .verify();

        verify(reviewService,times(4))
                .retrieveReviewsFlux(isA(Long.class));
    }

}