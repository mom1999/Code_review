package com.popita.codereviewagent.service.impl;

import com.popita.codereviewagent.dto.ReviewRequestDto;
import com.popita.codereviewagent.dto.ReviewResponseDto;
import com.popita.codereviewagent.entity.ReviewIssueEntity;
import com.popita.codereviewagent.entity.ReviewRequest;
import com.popita.codereviewagent.enums.ReviewStatus;
import com.popita.codereviewagent.model.RepositoryAnalysis;
import com.popita.codereviewagent.repository.ReviewRequestRepository;
import com.popita.codereviewagent.service.GitCloneService;
import com.popita.codereviewagent.service.RepositoryAnalysisService;
import com.popita.codereviewagent.service.ReviewIssueMapper;
import com.popita.codereviewagent.service.ReviewService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.UUID;


@Service
@Slf4j
public class ReviewServiceImpl implements ReviewService {


    private final ReviewRequestRepository reviewRequestRepository;

    private final GitCloneService gitCloneService;

    private final RepositoryAnalysisService repositoryAnalysisService;

    private final ReviewIssueMapper reviewIssueMapper;



    public ReviewServiceImpl(
            ReviewRequestRepository reviewRequestRepository,
            GitCloneService gitCloneService,
            RepositoryAnalysisService repositoryAnalysisService,
            ReviewIssueMapper reviewIssueMapper) {

        this.reviewRequestRepository = reviewRequestRepository;
        this.gitCloneService = gitCloneService;
        this.repositoryAnalysisService = repositoryAnalysisService;
        this.reviewIssueMapper = reviewIssueMapper;
    }



    @Override
    public ReviewResponseDto createReview(
            ReviewRequestDto request) {


        log.info(
                "Starting review for repository: {}",
                request.getRepositoryUrl()
        );


        ReviewRequest review =
                new ReviewRequest();


        review.setId(UUID.randomUUID());


        review.setRepositoryUrl(
                request.getRepositoryUrl()
        );


        review.setStatus(
                ReviewStatus.PENDING
        );


        review.setCreatedAt(
                LocalDateTime.now()
        );


        reviewRequestRepository.save(review);



        try {


            Path repositoryPath =
                    gitCloneService.cloneRepository(
                            review.getId(),
                            review.getRepositoryUrl()
                    );


            log.info(
                    "Repository cloned successfully: {}",
                    repositoryPath
            );



            RepositoryAnalysis analysis =
                    repositoryAnalysisService
                            .analyzeRepository(repositoryPath);



            log.info(
                    "Analysis completed. Classes: {}, Issues: {}",
                    analysis.getClasses().size(),
                    analysis.getIssues().size()
            );



            // Save issues

            analysis.getIssues()
                    .forEach(issue -> {

                        ReviewIssueEntity entity =
                                reviewIssueMapper.toEntity(
                                        issue,
                                        review
                                );


                        review.getIssues()
                                .add(entity);

                    });



            log.info(
                    "Issues attached before save: {}",
                    review.getIssues().size()
            );



            review.setStatus(
                    ReviewStatus.COMPLETED
            );


            review.setCompletedAt(
                    LocalDateTime.now()
            );


            reviewRequestRepository.save(review);



        } catch(Exception e) {


            log.error(
                    "Review failed for repository: {}",
                    review.getRepositoryUrl(),
                    e
            );


            review.setStatus(
                    ReviewStatus.FAILED
            );


            review.setCompletedAt(
                    LocalDateTime.now()
            );


            reviewRequestRepository.save(review);


            throw e;
        }



        return new ReviewResponseDto(
                review.getId(),
                review.getStatus()
        );
    }
}