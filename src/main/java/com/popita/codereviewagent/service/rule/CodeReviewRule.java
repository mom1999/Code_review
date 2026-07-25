package com.popita.codereviewagent.service.rule;

import com.github.javaparser.ast.CompilationUnit;
import com.popita.codereviewagent.model.ClassAnalysis;
import com.popita.codereviewagent.model.ReviewIssue;

import java.util.List;

public interface CodeReviewRule {

    List<ReviewIssue> review(CompilationUnit cu, ClassAnalysis analysis);

}