package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

class ReviewServiceTest {

    WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8080/movies")
            .build();

    ReviewService reviewService = new ReviewService(webClient);

    @Test
    void retrieveReviewsFlux_RestCliente() {

        var fluxReview = reviewService.retrieveReviewsFlux_RestCliente(1L).doOnNext(review -> {
            System.out.println(review.getReviewId());
        }).log();

        StepVerifier.create(fluxReview)
                .assertNext(
                        review -> assertEquals("Nolan is the real superhero",review.getComment())
                ).verifyComplete();
    }

}