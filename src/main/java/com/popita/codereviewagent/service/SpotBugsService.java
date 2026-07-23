package com.popita.codereviewagent.service;

import java.nio.file.Path;
import java.util.List;
import com.popita.codereviewagent.model.BugReport;

public interface SpotBugsService {

    List<BugReport> analyze(Path repositoryPath);

}