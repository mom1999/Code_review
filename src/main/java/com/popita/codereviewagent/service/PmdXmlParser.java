package com.popita.codereviewagent.service;

import com.popita.codereviewagent.model.ReviewIssue;

import java.nio.file.Path;
import java.util.List;

public interface PmdXmlParser {

    List<ReviewIssue> parse(Path pmdXml);
}
