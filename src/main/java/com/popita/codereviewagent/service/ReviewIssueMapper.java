package com.popita.codereviewagent.service;

import com.popita.codereviewagent.entity.ReviewIssueEntity;
import com.popita.codereviewagent.entity.ReviewRequest;
import com.popita.codereviewagent.model.ReviewIssue;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ReviewIssueMapper {


    public ReviewIssueEntity toEntity(
            ReviewIssue issue,
            ReviewRequest review) {


        ReviewIssueEntity entity =
                new ReviewIssueEntity();


        entity.setId(UUID.randomUUID());

        entity.setTool(issue.getTool());

        entity.setRule(issue.getRule());

        entity.setFileName(issue.getFileName());

        entity.setLineNumber(
                issue.getLineNumber()
        );

        entity.setSeverity(
                issue.getSeverity()
        );

        entity.setCategory(
                issue.getCategory()
        );

        entity.setMessage(
                issue.getMessage()
        );


        entity.setReviewRequest(review);


        return entity;
    }
}