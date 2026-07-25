package com.popita.codereviewagent.service.impl;
import com.popita.codereviewagent.service.GitCloneService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Slf4j
@Service
public class GitCloneServiceImpl implements GitCloneService {
    @Override
    public Path cloneRepository(UUID reviewId, String repositoryUrl) {


        try{

            //Create Temporary directory
            Path cloneDirectory = Files.createTempDirectory(reviewId.toString());

            //Create repo
            Git.cloneRepository()
                    .setURI(repositoryUrl)
                    .setDirectory(cloneDirectory.toFile())
                    .call();
            log.info("Repository cloned to: " + cloneDirectory);
            return  cloneDirectory;
        }
        catch (Exception e){
            throw new RuntimeException("Failed to clone the repo");
        }




    }
}
