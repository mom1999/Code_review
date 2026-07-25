package com.popita.codereviewagent.service.impl;

import com.popita.codereviewagent.service.CompilationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Service
public class CompilationServiceImpl implements CompilationService {
    @Override
    public Path compileRepository(Path repositoryPath) {

        try {

            Path pom = Files.walk(repositoryPath)
                    .filter(path -> path.getFileName().toString().equals("pom.xml"))
                    .findFirst()
                    .orElse(null);

            if (pom != null) {
                log.info("Maven project detected");
                log.info("pom.xml found at : " + pom);

                return pom.getParent();
            }

            Path gradle = Files.walk(repositoryPath)
                    .filter(path ->
                            path.getFileName().toString().equals("build.gradle")
                                    || path.getFileName().toString().equals("build.gradle.kts"))
                    .findFirst()
                    .orElse(null);

            if (gradle != null) {
                log.info("Gradle project detected");
                log.info("build file found at : " + gradle);

                return gradle.getParent();
            }

            throw new RuntimeException("Unsupported build system");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}