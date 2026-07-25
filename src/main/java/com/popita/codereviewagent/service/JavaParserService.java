package com.popita.codereviewagent.service;

import com.popita.codereviewagent.model.ParserResult;

import java.nio.file.Path;

public interface JavaParserService {

    ParserResult analyze(Path javaFile);

}