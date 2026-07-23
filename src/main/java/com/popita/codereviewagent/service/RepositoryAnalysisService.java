package com.popita.codereviewagent.service;

import com.popita.codereviewagent.model.RepositoryAnalysis;

import java.nio.file.Path;

public interface RepositoryAnalysisService {

    RepositoryAnalysis analyzeRepository(Path repositoryPath);

}