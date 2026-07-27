package com.popita.codereviewagent.service.impl;

import com.github.javaparser.ast.CompilationUnit;
import com.popita.codereviewagent.model.ClassAnalysis;
import com.popita.codereviewagent.model.ReviewIssue;
import com.popita.codereviewagent.service.AiReviewService;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiReviewServiceImpl implements AiReviewService {
    private final ChatClient chatClient;
    @Override
    public List<ReviewIssue> analyze(CompilationUnit compilationUnit, ClassAnalysis classAnalysis, List<ReviewIssue> existingIssues) {
        String code  = existingIssues.stream()
                .map(issue -> """
                Tool: %s
                Rule: %s
                File: %s
                Line: %d
                Severity: %s
                Category: %s
                Message: %s
                """.formatted(
                        issue.getTool(),
                        issue.getRule(),
                        issue.getFileName(),
                        issue.getLineNumber(),
                        issue.getSeverity(),
                        issue.getCategory(),
                        issue.getMessage()
                ))
                .collect(Collectors.joining("\n-----------------------\n"));;


        String prompt = """
        You are a Senior Java Spring Boot Code Reviewer.

        Below are issues reported by PMD and SpotBugs.

        %s

        For each issue explain:
        1. What the issue means.
        2. Why it matters.
        3. How to fix it.
        4. Give a Java example.

        Return ONLY valid JSON.
        """.formatted(code);

        String response = chatClient.prompt()
                .user(prompt)
                .call()
                .content();

        log.debug(response);


        ReviewIssue issue = new ReviewIssue();

        issue.setTool("AI");
        issue.setRule("AI_REVIEW");
        issue.setSeverity("INFO");
        issue.setCategory("AI");
        issue.setMessage(response);

        return List.of(issue);

    }
}
