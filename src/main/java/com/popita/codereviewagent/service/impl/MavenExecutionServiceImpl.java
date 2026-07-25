package com.popita.codereviewagent.service.impl;

import com.popita.codereviewagent.service.MavenExecutionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Service
public class MavenExecutionServiceImpl implements MavenExecutionService {

    @Override
    public void runPmd(Path repositoryPath) {

        try {

            ProcessBuilder processBuilder =
                    new ProcessBuilder(
                            "E:\\Spring_boot\\apache-maven-3.9.10-bin\\apache-maven-3.9.10\\bin\\mvn.cmd",
                            "pmd:pmd"
                    );

            processBuilder.directory(repositoryPath.toFile());

            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();

            BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(process.getInputStream()));

            String line;

            while ((line = reader.readLine()) != null) {
                log.info(line);
            }

            int exitCode = process.waitFor();
            Path pmdReport = repositoryPath.resolve("target").resolve("pmd.xml");
            log.info("PMD Report Exists : " + Files.exists(pmdReport));
            log.info("PMD Report Path : " + pmdReport);
            log.info("PMD Exit Code : " + exitCode);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}