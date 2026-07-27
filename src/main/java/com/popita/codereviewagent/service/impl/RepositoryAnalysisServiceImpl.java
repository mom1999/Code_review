package com.popita.codereviewagent.service.impl;


import com.popita.codereviewagent.model.ClassAnalysis;
import com.popita.codereviewagent.model.ParserResult;
import com.popita.codereviewagent.model.RepositoryAnalysis;
import com.popita.codereviewagent.model.ReviewIssue;
import com.popita.codereviewagent.service.*;
import com.popita.codereviewagent.service.review.ReviewEngine;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
public class RepositoryAnalysisServiceImpl
        implements RepositoryAnalysisService {


    private final RepositoryScannerService repositoryScannerService;

    private final JavaParserService javaParserService;

    private final CompilationService compilationService;

    private final ReviewEngine reviewEngine;

    private final List<CodeAnalyzer> analyzers;

    private final AiReviewService aiReviewService;

    public RepositoryAnalysisServiceImpl(
            RepositoryScannerService repositoryScannerService,
            JavaParserService javaParserService,
            CompilationService compilationService,
            ReviewEngine reviewEngine,
            List<CodeAnalyzer> analyzers, AiReviewService aiReviewService) {


        this.repositoryScannerService = repositoryScannerService;

        this.javaParserService = javaParserService;

        this.compilationService = compilationService;

        this.reviewEngine = reviewEngine;

        this.analyzers = analyzers;
        this.aiReviewService = aiReviewService;
    }



    @Override
    public RepositoryAnalysis analyzeRepository(
            Path repositoryPath) {


        log.info(
                "Starting repository analysis: {}",
                repositoryPath
        );


        compilationService.compileRepository(
                repositoryPath
        );


        RepositoryAnalysis repositoryAnalysis =
                new RepositoryAnalysis();

        Map<String, ParserResult> parserResults = new HashMap<>();

        List<Path> javaFiles =
                repositoryScannerService
                        .findJavaFiles(repositoryPath);



        log.info(
                "Java files found: {}",
                javaFiles.size()
        );



        for(Path javaFile : javaFiles) {


            ParserResult parserResult =
                    javaParserService.analyze(javaFile);

            //store compilation unit for the AI communication
            parserResults.put(
                    parserResult.getClassAnalysis().getClassName(),
                    parserResult
            );

            ClassAnalysis classAnalysis =
                    parserResult.getClassAnalysis();



            List<ReviewIssue> customIssues =
                    reviewEngine.review(
                            parserResult.getCompilationUnit(),
                            classAnalysis
                    );



            classAnalysis.setIssues(
                    customIssues
            );


            repositoryAnalysis
                    .getIssues()
                    .addAll(customIssues);



            repositoryAnalysis
                    .getClasses()
                    .add(classAnalysis);

        }



        /*
          Execute all analyzers

          Currently:
             PMD

          Future:
             SpotBugs
             Checkstyle
             Dependency Check
        */


        for(CodeAnalyzer analyzer : analyzers) {


            List<ReviewIssue> issues =
                    analyzer.analyze(repositoryPath);



            log.info(
                    "{} found {} issues",
                    analyzer.getClass().getSimpleName(),
                    issues.size()
            );


            repositoryAnalysis
                    .getIssues()
                    .addAll(issues);

        }
        for (ClassAnalysis classAnalysis : repositoryAnalysis.getClasses()) {
            ParserResult parserResult =
                    parserResults.get(classAnalysis.getClassName());
            if(parserResult == null){
                continue;
            }
            List<ReviewIssue> existingIssues =repositoryAnalysis.getIssues().stream()
                    .toList();
            List<ReviewIssue> aiIssues =aiReviewService.analyze(parserResult.getCompilationUnit()
                    ,classAnalysis,existingIssues);

            repositoryAnalysis
                    .getIssues()
                    .addAll(aiIssues);
        }


        log.info(
                "Repository analysis completed. Total issues: {}",
                repositoryAnalysis.getIssues().size()
        );


        return repositoryAnalysis;
    }
}