package com.popita.codereviewagent.service;

import com.popita.codereviewagent.dto.ReviewRequestDto;
import com.popita.codereviewagent.dto.ReviewResponseDto;

public interface ReviewService {
    ReviewResponseDto createReview(ReviewRequestDto review);
}
