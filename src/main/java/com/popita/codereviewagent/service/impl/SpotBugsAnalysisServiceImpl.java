package com.popita.codereviewagent.service.impl;

import com.popita.codereviewagent.model.ReviewIssue;
import com.popita.codereviewagent.service.SpotBugsExecutionService;
import com.popita.codereviewagent.service.SpotBugsXmlParser;
import com.popita.codereviewagent.service.spotbugs.SpotBugsAnalysisService;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class SpotBugsAnalysisServiceImpl implements SpotBugsAnalysisService {
    private final SpotBugsXmlParser spotBugsXmlParser;
    private final SpotBugsExecutionService spotBugsExecutionService;
    public SpotBugsAnalysisServiceImpl(SpotBugsXmlParser spotBugsXmlParser, SpotBugsExecutionService spotBugsExecutionService) {
        this.spotBugsXmlParser = spotBugsXmlParser;
        this.spotBugsExecutionService = spotBugsExecutionService;
    }

    @Override
    public List<ReviewIssue> analyze(Path repositoryPath) {


        Path pomFile =
                repositoryPath.resolve("pom.xml");


        if(!Files.exists(pomFile)){
            return new ArrayList<>();
        }


        spotBugsExecutionService.runSpotBugs(repositoryPath);


        Path report =
                repositoryPath
                        .resolve("target")
                        .resolve("spotbugsXml.xml");


        if(!Files.exists(report)){
            return new ArrayList<>();
        }


        return spotBugsXmlParser.parse(report);
    }
}
