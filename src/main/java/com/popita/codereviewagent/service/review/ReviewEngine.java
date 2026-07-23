package com.popita.codereviewagent.service.review;

import com.popita.codereviewagent.model.ClassAnalysis;
import com.popita.codereviewagent.model.ReviewIssue;
import com.popita.codereviewagent.service.rule.CodeReviewRule;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewEngine {

    private final List<CodeReviewRule> rules;

    public ReviewEngine(List<CodeReviewRule> rules) {
        this.rules = rules;
    }

    public List<ReviewIssue> review(ClassAnalysis analysis) {

        List<ReviewIssue> issues = new ArrayList<>();

        for (CodeReviewRule rule : rules) {
            issues.addAll(rule.review(analysis));
        }

        return issues;
    }
}