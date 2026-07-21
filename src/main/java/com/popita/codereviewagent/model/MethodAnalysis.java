package com.popita.codereviewagent.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
public class MethodAnalysis {
    private  String methodName;
    private String returnType;
    private int parameterCount;

}
