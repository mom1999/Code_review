package com.popita.codereviewagent.service.impl;

import com.popita.codereviewagent.dto.ReviewRequestDto;
import com.popita.codereviewagent.dto.ReviewResponseDto;
import com.popita.codereviewagent.entity.ReviewRequest;
import com.popita.codereviewagent.enums.ReviewStatus;
import com.popita.codereviewagent.repository.ReviewRequestRepository;
import com.popita.codereviewagent.service.ReviewService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRequestRepository repository;

    public ReviewServiceImpl(ReviewRequestRepository repository) {
        this.repository = repository;
    }

    @Override
    public ReviewResponseDto createReview(ReviewRequestDto request) {
            ReviewRequest review = new ReviewRequest();
            review.setId(UUID.randomUUID());
            review.setRepositoryUrl(request.getRepositoryUrl());
            review.setStatus(ReviewStatus.PENDING);
            review.setCreatedAt(LocalDateTime.now());
            repository.save(review);


        return new ReviewResponseDto(
                review.getId(),
                review.getStatus()
        );
    }
}
