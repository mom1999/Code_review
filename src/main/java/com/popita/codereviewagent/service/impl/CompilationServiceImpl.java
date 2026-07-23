package com.popita.codereviewagent.service.impl;

import com.popita.codereviewagent.service.CompilationService;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;

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
                System.out.println("Maven project detected");
                System.out.println("pom.xml found at : " + pom);

                return pom.getParent();
            }

            Path gradle = Files.walk(repositoryPath)
                    .filter(path ->
                            path.getFileName().toString().equals("build.gradle")
                                    || path.getFileName().toString().equals("build.gradle.kts"))
                    .findFirst()
                    .orElse(null);

            if (gradle != null) {
                System.out.println("Gradle project detected");
                System.out.println("build file found at : " + gradle);

                return gradle.getParent();
            }

            throw new RuntimeException("Unsupported build system");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}