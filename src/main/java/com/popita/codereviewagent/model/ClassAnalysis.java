package com.popita.codereviewagent.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ClassAnalysis {
    private String className;
    private String packageName;
    private List<String> anotation = new ArrayList<>();
    private List<MethodAnalysis> methods = new ArrayList<>();
    private List<FieldAnalysis> fields = new ArrayList<>();
    private List<ReviewIssue> issues = new ArrayList<>();
}
