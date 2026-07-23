package com.popita.codereviewagent.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RepositoryAnalysis {
    private String repositoryUrl;

    private List<ClassAnalysis> classes =
            new ArrayList<>();
    private List<ReviewIssue> issues = new ArrayList<>();

}
