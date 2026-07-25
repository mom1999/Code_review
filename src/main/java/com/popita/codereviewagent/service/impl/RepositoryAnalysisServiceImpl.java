package com.popita.codereviewagent.service.impl;


import com.popita.codereviewagent.model.ClassAnalysis;
import com.popita.codereviewagent.model.ParserResult;
import com.popita.codereviewagent.model.RepositoryAnalysis;
import com.popita.codereviewagent.model.ReviewIssue;
import com.popita.codereviewagent.service.CompilationService;
import com.popita.codereviewagent.service.JavaParserService;
import com.popita.codereviewagent.service.RepositoryAnalysisService;
import com.popita.codereviewagent.service.RepositoryScannerService;
import com.popita.codereviewagent.service.CodeAnalyzer;
import com.popita.codereviewagent.service.review.ReviewEngine;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;


@Slf4j
@Service
public class RepositoryAnalysisServiceImpl
        implements RepositoryAnalysisService {


    private final RepositoryScannerService repositoryScannerService;

    private final JavaParserService javaParserService;

    private final CompilationService compilationService;

    private final ReviewEngine reviewEngine;

    private final List<CodeAnalyzer> analyzers;



    public RepositoryAnalysisServiceImpl(
            RepositoryScannerService repositoryScannerService,
            JavaParserService javaParserService,
            CompilationService compilationService,
            ReviewEngine reviewEngine,
            List<CodeAnalyzer> analyzers) {


        this.repositoryScannerService = repositoryScannerService;

        this.javaParserService = javaParserService;

        this.compilationService = compilationService;

        this.reviewEngine = reviewEngine;

        this.analyzers = analyzers;
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



        log.info(
                "Repository analysis completed. Total issues: {}",
                repositoryAnalysis.getIssues().size()
        );


        return repositoryAnalysis;
    }
}