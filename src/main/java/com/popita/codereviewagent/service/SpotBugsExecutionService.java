package com.popita.codereviewagent.service;

import java.nio.file.Path;

public interface SpotBugsExecutionService {
    void runSpotBugs(Path repositoryPath);
}
