package com.popita.codereviewagent.service.impl;

import com.popita.codereviewagent.model.ReviewIssue;
import com.popita.codereviewagent.service.SpotBugsXmlParser;
import org.springframework.stereotype.Service;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class SpotBugsXmlParserImpl implements SpotBugsXmlParser {

    @Override
    public List<ReviewIssue> parse(Path xmlPath) {

        List<ReviewIssue> issues = new ArrayList<>();

        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = builder.parse(xmlPath.toFile());
            document.getDocumentElement().normalize();

            NodeList bugInstances = document.getElementsByTagName("BugInstance");

            for (int i = 0; i < bugInstances.getLength(); i++) {

                Node node = bugInstances.item(i);

                if (node.getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }

                Element bug = (Element) node;

                ReviewIssue issue = new ReviewIssue();

                issue.setTool("SpotBugs");

                issue.setRule(bug.getAttribute("type"));

                issue.setSeverity(bug.getAttribute("priority"));

                NodeList shortMessages = bug.getElementsByTagName("ShortMessage");
                if (shortMessages.getLength() > 0) {
                    issue.setMessage(shortMessages.item(0).getTextContent());
                }

                NodeList sourceLines = bug.getElementsByTagName("SourceLine");
                if (sourceLines.getLength() > 0) {

                    Element source = (Element) sourceLines.item(0);

                    issue.setFileName(source.getAttribute("sourcefile"));

                    String line = source.getAttribute("start");

                    if (!line.isBlank()) {
                        issue.setLineNumber(Integer.parseInt(line));
                    }
                }

                issues.add(issue);
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse SpotBugs XML", e);
        }

        return issues;
    }
}