package com.popita.codereviewagent.service.impl;

import com.popita.codereviewagent.dto.ReviewRequestDto;
import com.popita.codereviewagent.dto.ReviewResponseDto;
import com.popita.codereviewagent.entity.ReviewRequest;
import com.popita.codereviewagent.enums.ReviewStatus;
import com.popita.codereviewagent.model.RepositoryAnalysis;
import com.popita.codereviewagent.repository.ReviewRequestRepository;
import com.popita.codereviewagent.service.GitCloneService;
import com.popita.codereviewagent.service.RepositoryAnalysisService;
import com.popita.codereviewagent.service.ReviewService;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRequestRepository reviewRequestRepository;
    private final GitCloneService gitCloneService;
    private final RepositoryAnalysisService repositoryAnalysisService;

    public ReviewServiceImpl(
            ReviewRequestRepository reviewRequestRepository,
            GitCloneService gitCloneService,
            RepositoryAnalysisService repositoryAnalysisService) {

        this.reviewRequestRepository = reviewRequestRepository;
        this.gitCloneService = gitCloneService;
        this.repositoryAnalysisService = repositoryAnalysisService;
    }

    @Override
    public ReviewResponseDto createReview(ReviewRequestDto request) {

        // Save review request
        ReviewRequest review = new ReviewRequest();
        review.setId(UUID.randomUUID());
        review.setRepositoryUrl(request.getRepositoryUrl());
        review.setStatus(ReviewStatus.PENDING);
        review.setCreatedAt(LocalDateTime.now());

        reviewRequestRepository.save(review);

        // Clone repository
        Path repositoryPath = gitCloneService.cloneRepository(
                review.getId(),
                review.getRepositoryUrl()
        );

        // Analyze repository
        RepositoryAnalysis repositoryAnalysis =
                repositoryAnalysisService.analyzeRepository(repositoryPath);

        repositoryAnalysis.setRepositoryUrl(request.getRepositoryUrl());

        // Print summary
        System.out.println("\n========================================");
        System.out.println("Repository : " + repositoryAnalysis.getRepositoryUrl());
        System.out.println("Total Classes : " + repositoryAnalysis.getClasses().size());

        repositoryAnalysis.getClasses().forEach(clazz -> {

            System.out.println("\n----------------------------------------");
            System.out.println("Class : " + clazz.getClassName());
            System.out.println("Package : " + clazz.getPackageName());
            System.out.println("Annotations : " + clazz.getAnotation());

            System.out.println("\nFields");

            clazz.getFields().forEach(field ->
                    System.out.println(
                            field.getFieldName()
                                    + " : "
                                    + field.getFieldType()
                    )
            );

            System.out.println("\nMethods");

            clazz.getMethods().forEach(method ->
                    System.out.println(
                            method.getMethodName()
                                    + " | "
                                    + method.getReturnType()
                                    + " | Params : "
                                    + method.getParameterCount()
                    )
            );
        });

        // Mark review complete
        review.setStatus(ReviewStatus.COMPLETED);
        reviewRequestRepository.save(review);

        return new ReviewResponseDto(
                review.getId(),
                review.getStatus()
        );
    }
}