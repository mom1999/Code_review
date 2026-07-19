package com.popita.codereviewagent.service;

import java.nio.file.Path;
import java.util.UUID;

public interface GitCloneService {

    Path cloneRepository(UUID reviewId, String repositoryUrl);
}
