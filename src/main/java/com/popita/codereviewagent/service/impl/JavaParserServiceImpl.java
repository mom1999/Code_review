package com.popita.codereviewagent.service.impl;

import com.github.javaparser.*;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.popita.codereviewagent.model.ClassAnalysis;
import com.popita.codereviewagent.model.FieldAnalysis;
import com.popita.codereviewagent.model.MethodAnalysis;
import com.popita.codereviewagent.service.JavaParserService;
import org.springframework.stereotype.Service;
import com.github.javaparser.StaticJavaParser;
import java.io.IOException;
import java.nio.file.Path;

@Service
public class JavaParserServiceImpl implements JavaParserService {
    ClassAnalysis analysis = new ClassAnalysis();
    @Override
    public ClassAnalysis analyze(Path javaFile) {

        System.out.println("Parsing: " + javaFile);
        System.out.println(
                "JavaParser Version = " +
                        StaticJavaParser.class.getPackage().getImplementationVersion()
        );
            try{
                ParserConfiguration configuration = new ParserConfiguration();
                configuration.setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_21);

                JavaParser parser = new JavaParser(configuration);

                ParseResult<CompilationUnit> result = parser.parse(javaFile);

                if(result.isSuccessful() && result.getResult().isPresent()){

                    CompilationUnit cu = result.getResult().get();
                    analysis.setClassName(cu.getPrimaryTypeName().orElse("Unknown"));
                    cu.getPackageDeclaration().ifPresent(pkg ->analysis.setPackageName(pkg.getNameAsString()));
                    cu.findAll(MethodDeclaration.class).forEach(
                            method ->{
                                MethodAnalysis methodAnalysis = new MethodAnalysis();
                                methodAnalysis.setMethodName(method.getNameAsString());
                                methodAnalysis.setReturnType(method.getType().asString());
                                methodAnalysis.setParameterCount(method.getParameters().size());
                                analysis.getMethods().add(methodAnalysis);
                            }
                    );
                    cu.findAll(ClassOrInterfaceDeclaration.class)
                                    .forEach(c ->{
                                        c.getAnnotations().forEach(anotation->
                                                analysis.getAnotation().add(anotation.getNameAsString())
                                                );
                                    });

                    cu.findAll(FieldDeclaration.class)
                                    .forEach(field ->{
                                        for(VariableDeclarator variableDeclarator : field.getVariables()){
                                            FieldAnalysis fieldAnalysis = new FieldAnalysis();
                                            fieldAnalysis.setFieldName(variableDeclarator.getNameAsString());
                                            fieldAnalysis.setFieldType(variableDeclarator.getType().asString());
                                            analysis.getFields().add(fieldAnalysis);

                                        }
                                            }

                                    );

                    System.out.println("Class : " +
                            cu.getPrimaryTypeName().orElse("Unknown"));

                }
                else{

                    result.getProblems().forEach(System.out::println);

                }
                return analysis;
            }
            catch (Exception  e){
                throw new RuntimeException(e);
            }
    }
}
