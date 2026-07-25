package com.popita.codereviewagent.service.rule;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.popita.codereviewagent.model.ClassAnalysis;
import com.popita.codereviewagent.model.ReviewIssue;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SystemOutRule implements CodeReviewRule {


    @Override
    public List<ReviewIssue> review(
            CompilationUnit cu,
            ClassAnalysis analysis) {


        List<ReviewIssue> issues =
                new ArrayList<>();


        cu.findAll(MethodCallExpr.class)
                .forEach(methodCall -> {


                    if(methodCall.toString()
                            .startsWith("System.out")) {


                        int line =
                                methodCall.getBegin()
                                        .map(position ->
                                                position.line)
                                        .orElse(0);



                        ReviewIssue issue =
                                new ReviewIssue();


                        issue.setTool(
                                "Custom Rule"
                        );


                        issue.setRule(
                                "Avoid System.out"
                        );


                        issue.setFileName(
                                analysis.getClassName()
                        );


                        issue.setLineNumber(
                                line
                        );


                        issue.setSeverity(
                                "WARNING"
                        );


                        issue.setCategory(
                                "Code Quality"
                        );


                        issue.setMessage(
                                "Avoid using log.info(). Use a logger instead."
                        );


                        issues.add(issue);
                    }

                });


        return issues;
    }
}