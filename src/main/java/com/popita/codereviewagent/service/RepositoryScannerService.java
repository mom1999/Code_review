package com.popita.codereviewagent.service;

import java.nio.file.Path;
import java.util.List;

public interface RepositoryScannerService {
    List<Path> findJavaFiles(Path repositoryPath);
}
