package com.popita.codereviewagent.service.impl;

import com.popita.codereviewagent.dto.ReviewRequestDto;
import com.popita.codereviewagent.dto.ReviewResponseDto;
import com.popita.codereviewagent.entity.ReviewRequest;
import com.popita.codereviewagent.enums.ReviewStatus;
import com.popita.codereviewagent.model.ClassAnalysis;
import com.popita.codereviewagent.model.RepositoryAnalysis;
import com.popita.codereviewagent.repository.ReviewRequestRepository;
import com.popita.codereviewagent.service.GitCloneService;
import com.popita.codereviewagent.service.JavaParserService;
import com.popita.codereviewagent.service.RepositoryScannerService;
import com.popita.codereviewagent.service.ReviewService;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRequestRepository repository;
    private final GitCloneService gitCloneService;
    private final RepositoryScannerService repositoryScannerService;
    private final JavaParserService javaParserService;
    private final ReviewRequestRepository reviewRequestRepository;
    public ReviewServiceImpl(ReviewRequestRepository repository, GitCloneService gitCloneService, RepositoryScannerService repositoryScannerService, JavaParserService javaParserService, ReviewRequestRepository reviewRequestRepository) {
        this.repository = repository;
        this.gitCloneService = gitCloneService;
        this.repositoryScannerService = repositoryScannerService;
        this.javaParserService = javaParserService;
        this.reviewRequestRepository = reviewRequestRepository;
    }

    @Override
    public ReviewResponseDto createReview(ReviewRequestDto request) {
            ReviewRequest review = new ReviewRequest();
            review.setId(UUID.randomUUID());
            review.setRepositoryUrl(request.getRepositoryUrl());
            review.setStatus(ReviewStatus.PENDING);
            review.setCreatedAt(LocalDateTime.now());
            repository.save(review);

        //create repository analysis
        RepositoryAnalysis repositoryAnalysis = new RepositoryAnalysis();
        repositoryAnalysis.setRepositoryUrl(request.getRepositoryUrl());

        // Clone the Git repository
        Path repositoryPath = gitCloneService.cloneRepository(
                review.getId(),
                review.getRepositoryUrl()
        );
        List<Path> javaFiles =
                repositoryScannerService.findJavaFiles(repositoryPath);
        System.out.println("Java files found: " + javaFiles.size());
        javaFiles.forEach(System.out::println);
        // Analyze every Java file
        for (Path javaFile : javaFiles) {
            ClassAnalysis analysis =
                    javaParserService.analyze(javaFile);

            repositoryAnalysis.getClasses().add(analysis);
        }



        // Print Summary
        System.out.println("\n========================================");
        System.out.println("Repository : "
                + repositoryAnalysis.getRepositoryUrl());

        System.out.println("Total Classes : "
                + repositoryAnalysis.getClasses().size());

        repositoryAnalysis.getClasses().forEach(clazz -> {

            System.out.println("\n----------------------------------------");

            System.out.println("Class : "
                    + clazz.getClassName());

            System.out.println("Package : "
                    + clazz.getPackageName());

            System.out.println("Annotations : "
                    + clazz.getAnotation());

            System.out.println("\nFields");

            clazz.getFields().forEach(field ->

                    System.out.println(
                            field.getFieldName()
                                    + " : "
                                    + field.getFieldType())

            );

            System.out.println("\nMethods");

            clazz.getMethods().forEach(method ->

                    System.out.println(
                            method.getMethodName()
                                    + " | "
                                    + method.getReturnType()
                                    + " | Params : "
                                    + method.getParameterCount())

            );

        });

        // Update Status
        review.setStatus(ReviewStatus.COMPLETED);
        reviewRequestRepository.save(review);





        return new ReviewResponseDto(
                review.getId(),
                review.getStatus()
        );
    }
}
