package com.popita.codereviewagent.service;

import java.nio.file.Path;

public interface CompilationService {

    Path compileRepository(Path repositoryPath);

}