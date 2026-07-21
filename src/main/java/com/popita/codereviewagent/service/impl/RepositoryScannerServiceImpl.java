package com.popita.codereviewagent.service.impl;

import com.popita.codereviewagent.service.RepositoryScannerService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RepositoryScannerServiceImpl implements RepositoryScannerService {
    @Override
    public List<Path> findJavaFiles(Path repositoryPath) {
        try {
            return Files.walk(repositoryPath)
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".java"))
                    .collect(Collectors.toList());
        }
        catch (IOException e){
            throw new RuntimeException("Failed to scan  repository ",e);
        }

    }
}
