package com.popita.codereviewagent.service.impl;

import com.popita.codereviewagent.model.ReviewIssue;
import com.popita.codereviewagent.service.MavenExecutionService;
import com.popita.codereviewagent.service.PmdXmlParser;
import com.popita.codereviewagent.service.pmd.PmdAnalysisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class PmdAnalysisServiceImpl
        implements PmdAnalysisService {
    private final MavenExecutionService mavenExecutionService;
    private final PmdXmlParser pmdXmlParser;
    public PmdAnalysisServiceImpl(MavenExecutionService mavenExecutionService, PmdXmlParser pmdXmlParser) {
        this.mavenExecutionService = mavenExecutionService;
        this.pmdXmlParser = pmdXmlParser;
    }

    @Override
    public List<ReviewIssue> analyze(Path repositoryPath) {
        Path pomFile = repositoryPath.resolve("pom.xml");

        if (!Files.exists(pomFile)) {

            log.info("Skipping PMD. pom.xml not found.");

            return new ArrayList<>();
        }
        mavenExecutionService.runPmd(repositoryPath);
        Path report = repositoryPath
                .resolve("target")
                .resolve("pmd.xml");

        return pmdXmlParser.parse(report);
    }

}
