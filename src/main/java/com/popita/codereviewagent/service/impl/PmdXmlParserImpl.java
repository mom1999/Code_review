package com.popita.codereviewagent.service.impl;

import com.popita.codereviewagent.model.ReviewIssue;
import com.popita.codereviewagent.service.PmdXmlParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class PmdXmlParserImpl implements PmdXmlParser {


    @Override
    public List<ReviewIssue> parse(Path pmdXml) {


        List<ReviewIssue> issues = new ArrayList<>();


        try {


            DocumentBuilderFactory factory =
                    DocumentBuilderFactory.newInstance();


            DocumentBuilder builder =
                    factory.newDocumentBuilder();


            Document document =
                    builder.parse(pmdXml.toFile());


            document.getDocumentElement()
                    .normalize();



            NodeList violationNodes =
                    document.getElementsByTagName("violation");



            log.info(
                    "PMD Violations found : "
                            + violationNodes.getLength()
            );



            for(int i = 0; i < violationNodes.getLength(); i++) {


                Element violation =
                        (Element) violationNodes.item(i);



                String rule =
                        violation.getAttribute("rule");



                String priority =
                        violation.getAttribute("priority");



                String beginLine =
                        violation.getAttribute("beginline");



                String fileName =
                        violation
                                .getParentNode()
                                .getAttributes()
                                .getNamedItem("name")
                                .getNodeValue();



                String message =
                        violation.getTextContent();



                ReviewIssue issue =
                        new ReviewIssue();


                issue.setTool("PMD");

                issue.setRule(rule);

                issue.setFileName(fileName);

                issue.setLineNumber(
                        beginLine.isEmpty()
                                ? 0
                                : Integer.parseInt(beginLine)
                );


                issue.setSeverity(priority);

                issue.setCategory("Static Analysis");

                issue.setMessage(message);



                issues.add(issue);

            }



        } catch(Exception e) {

            throw new RuntimeException(
                    "Failed to parse PMD report",
                    e
            );
        }



        return issues;
    }
}