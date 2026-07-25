package com.popita.codereviewagent.service.pmd;

import com.popita.codereviewagent.model.ReviewIssue;
import com.popita.codereviewagent.service.CodeAnalyzer;

import java.nio.file.Path;
import java.util.List;

public interface PmdAnalysisService extends CodeAnalyzer {

    List<ReviewIssue> analyze(Path repositoryPath);

}