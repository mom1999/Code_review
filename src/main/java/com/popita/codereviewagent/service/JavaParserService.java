package com.popita.codereviewagent.service;

import com.popita.codereviewagent.model.ClassAnalysis;

import java.nio.file.Path;

public interface JavaParserService {
    ClassAnalysis analyze(Path javaFile);
}
