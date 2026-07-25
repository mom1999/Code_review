package com.popita.codereviewagent.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewIssue {

    private String tool;

    private String rule;

    private String fileName;

    private int lineNumber;

    private String severity;

    private String category;

    private String message;
}