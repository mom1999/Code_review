package com.popita.codereviewagent.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class ReviewIssue {
    private String tool;
    private String fileName;
    private int lineNumber;
    private String severity;
    private String message;
}
