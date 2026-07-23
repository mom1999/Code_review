package com.popita.codereviewagent.service.impl;

import com.popita.codereviewagent.model.ClassAnalysis;
import com.popita.codereviewagent.model.RepositoryAnalysis;
import com.popita.codereviewagent.model.ReviewIssue;
import com.popita.codereviewagent.service.CompilationService;
import com.popita.codereviewagent.service.JavaParserService;
import com.popita.codereviewagent.service.RepositoryAnalysisService;
import com.popita.codereviewagent.service.RepositoryScannerService;
import com.popita.codereviewagent.service.review.ReviewEngine;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;

@Service
public class RepositoryAnalysisServiceImpl implements RepositoryAnalysisService {

    private final RepositoryScannerService repositoryScannerService;
    private final JavaParserService javaParserService;
    private final CompilationService compilationService;
    private final ReviewEngine reviewEngine;
    public RepositoryAnalysisServiceImpl(
            RepositoryScannerService repositoryScannerService,
            JavaParserService javaParserService, CompilationService compilationService, ReviewEngine reviewEngine) {

        this.repositoryScannerService = repositoryScannerService;
        this.javaParserService = javaParserService;
        this.compilationService = compilationService;
        this.reviewEngine = reviewEngine;
    }


    @Override
    public RepositoryAnalysis analyzeRepository(Path repositoryPath) {
        compilationService.compileRepository(repositoryPath);
        RepositoryAnalysis repositoryAnalysis = new RepositoryAnalysis();

        List<Path> javaFiles =
                repositoryScannerService.findJavaFiles(repositoryPath);

        System.out.println("Java files found : " + javaFiles.size());

        javaFiles.forEach(System.out::println);

        for (Path javaFile : javaFiles) {

            ClassAnalysis classAnalysis =
                    javaParserService.analyze(javaFile);
// Execute all review rules
            List<ReviewIssue> issues = reviewEngine.review(classAnalysis);

// Store the issues
            classAnalysis.setIssues(issues);
            repositoryAnalysis.getClasses().add(classAnalysis);

            //print
            if (issues.isEmpty()) {
                System.out.println("No issues found in " + classAnalysis.getClassName());
            } else {

                System.out.println("\nIssues found in " + classAnalysis.getClassName());

                for (ReviewIssue issue : issues) {
                    System.out.println("--------------------------------");
                    System.out.println("Rule : " + issue.getTool());
                    System.out.println("Severity : " + issue.getSeverity());
                    System.out.println("Message : " + issue.getMessage());
                    System.out.println("File : " + issue.getFileName());
                    System.out.println("Line : " + issue.getLineNumber());
                }
            }
        }

        return repositoryAnalysis;
    }
}