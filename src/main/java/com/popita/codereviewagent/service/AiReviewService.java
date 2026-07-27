package com.popita.codereviewagent.service;

import com.github.javaparser.ast.CompilationUnit;
import com.popita.codereviewagent.model.ClassAnalysis;
import com.popita.codereviewagent.model.ReviewIssue;

import java.util.List;

public interface AiReviewService {
    List<ReviewIssue> analyze(
            CompilationUnit compilationUnit,
            ClassAnalysis classAnalysis,
            List<ReviewIssue> existingIssues
    );
}
