package com.popita.codereviewagent.repository;


import com.popita.codereviewagent.entity.ReviewIssueEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface ReviewIssueRepository
        extends JpaRepository<ReviewIssueEntity, UUID> {

}