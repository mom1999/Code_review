package com.popita.codereviewagent.service.rule;

import com.popita.codereviewagent.model.ClassAnalysis;
import com.popita.codereviewagent.model.ReviewIssue;

import java.util.List;

public interface CodeReviewRule {

    List<ReviewIssue> review(ClassAnalysis analysis);

}