package com.popita.codereviewagent.controller;

import com.popita.codereviewagent.dto.ReviewRequestDto;
import com.popita.codereviewagent.dto.ReviewResponseDto;
import com.popita.codereviewagent.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    ReviewResponseDto createReview( @Valid @RequestBody ReviewRequestDto review){
        return reviewService.createReview(review);

    }
}
