package com.popita.codereviewagent.model;

import com.github.javaparser.ast.CompilationUnit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ParserResult {

    private CompilationUnit compilationUnit;
    private ClassAnalysis classAnalysis;

}