package com.popita.codereviewagent.service.impl;

import com.popita.codereviewagent.model.BugReport;
import com.popita.codereviewagent.service.SpotBugsService;

import java.nio.file.Path;
import java.util.List;

public class SpotBugsServiceImpl implements SpotBugsService {

    @Override
    public List<BugReport> analyze(Path repositoryPath) {
        return List.of();
    }
}
