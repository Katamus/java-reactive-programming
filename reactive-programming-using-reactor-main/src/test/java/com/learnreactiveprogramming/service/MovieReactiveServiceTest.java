package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

class MovieReactiveServiceTest {

    private MovieInfoService movieInfoService = new MovieInfoService();
    private ReviewService reviewService = new ReviewService();

    private RevenueService revenueService = new RevenueService();

    MovieReactiveService movieReactiveService = new MovieReactiveService(movieInfoService,reviewService,revenueService);

    @Test
    void getAllMovies() {

        var movieFlux = movieReactiveService.getAllMovies();

        StepVerifier.create(movieFlux)
                .assertNext(movie -> {
                    assertEquals("Batman Begins", movie.getMovie().getName());
                    assertEquals(2,movie.getReviewList().size());
                })
                .assertNext(movie -> {
                    assertEquals("The Dark Knight", movie.getMovie().getName());
                    assertEquals(2,movie.getReviewList().size());
                })
                .assertNext(movie -> {
                    assertEquals("Dark Knight Rises", movie.getMovie().getName());
                    assertEquals(2,movie.getReviewList().size());
                })
                .verifyComplete();

    }

    @Test
    void getMovieById() {

        Long movieId = 100L;

        var movieMono = movieReactiveService.getMovieById(movieId);

        StepVerifier.create(movieMono)
                .assertNext(movie -> {
                    assertEquals("Batman Begins", movie.getMovie().getName());
                    assertEquals(2,movie.getReviewList().size());
                })
                .verifyComplete();

    }

    @Test
    void getMovieById_flatMap() {
        Long movieId = 100L;
        var movieMono = movieReactiveService.getMovieById_flatMap(movieId);
        StepVerifier.create(movieMono)
                .assertNext(movie -> {
                    assertEquals("Batman Begins", movie.getMovie().getName());
                    assertEquals(2,movie.getReviewList().size());
                })
                .verifyComplete();
    }

    @Test
    void getMovieById_withRevenenue() {

        Long movieId = 100L;

        var movieMono = movieReactiveService.getMovieById_withRevenenue(movieId);

        StepVerifier.create(movieMono)
                .assertNext(movie -> {
                    assertEquals("Batman Begins", movie.getMovie().getName());
                    assertEquals(2,movie.getReviewList().size());
                    assertNotNull(movie.getRevenue());
                })
                .verifyComplete();
    }
}