package com.popita.codereviewagent.service.rule;

import com.popita.codereviewagent.model.ClassAnalysis;
import com.popita.codereviewagent.model.ReviewIssue;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LargeClassRule implements CodeReviewRule {

    @Override
    public List<ReviewIssue> review(ClassAnalysis analysis) {

        List<ReviewIssue> issues = new ArrayList<>();

        if (analysis.getMethods().size() > 1) {
            ReviewIssue issue = new ReviewIssue();

            issue.setTool("Custom Rule");
            issue.setFileName(analysis.getClassName());
            issue.setLineNumber(0); // We'll improve this later
            issue.setSeverity("WARNING");
            issue.setMessage("Class has more than 1 method.");

            issues.add(issue);
        }

        return issues;
    }
}