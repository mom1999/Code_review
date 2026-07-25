package com.popita.codereviewagent.service;

import java.nio.file.Path;

public interface MavenExecutionService {

    void runPmd(Path repositoryPath);

}