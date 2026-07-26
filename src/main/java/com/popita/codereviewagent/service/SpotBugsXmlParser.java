package com.popita.codereviewagent.service;

import com.popita.codereviewagent.model.ReviewIssue;

import java.nio.file.Path;
import java.util.List;

public interface SpotBugsXmlParser {
    List<ReviewIssue> parse(Path xmlPath);
}
