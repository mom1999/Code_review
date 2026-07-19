package com.popita.codereviewagent.service.impl;

import com.popita.codereviewagent.dto.ReviewRequestDto;
import com.popita.codereviewagent.dto.ReviewResponseDto;
import com.popita.codereviewagent.entity.ReviewRequest;
import com.popita.codereviewagent.enums.ReviewStatus;
import com.popita.codereviewagent.repository.ReviewRequestRepository;
import com.popita.codereviewagent.service.GitCloneService;
import com.popita.codereviewagent.service.ReviewService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRequestRepository repository;
    private final GitCloneService gitCloneService;

    public ReviewServiceImpl(ReviewRequestRepository repository, GitCloneService gitCloneService) {
        this.repository = repository;
        this.gitCloneService = gitCloneService;
    }

    @Override
    public ReviewResponseDto createReview(ReviewRequestDto request) {
            ReviewRequest review = new ReviewRequest();
            review.setId(UUID.randomUUID());
            review.setRepositoryUrl(request.getRepositoryUrl());
            review.setStatus(ReviewStatus.PENDING);
            review.setCreatedAt(LocalDateTime.now());
            repository.save(review);

        // Clone the Git repository
        gitCloneService.cloneRepository(
                review.getId(),
                review.getRepositoryUrl()
        );

        return new ReviewResponseDto(
                review.getId(),
                review.getStatus()
        );
    }
}
