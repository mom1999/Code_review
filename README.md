# AI-Powered Code Review Agent

A Java/Spring Boot based code review tool that combines **static code analysis with Generative AI**.

The idea behind this project is simple: tools like **PMD and SpotBugs are good at finding problems in code, but their reports are not always easy to understand**. This project takes those findings and uses **Google Gemini** to explain what the issue means, why it matters, and how it can be fixed.

---

## 💡 Why I Built This

While working with large Java codebases, static-analysis tools can generate a lot of findings. Developers still need to go through those findings and understand whether they are important and how to fix them.

So I wanted to build a small system that follows this approach:


Git Repository
      ↓
Static Analysis
      ↓
Find Issues
      ↓
Store Issues
      ↓
Gemini AI
      ↓
Explain the Issues
      ↓
Developer Understands & Fixes


The goal is **not to replace PMD or SpotBugs with AI**.

Instead, the project uses static-analysis tools to reliably find issues and uses AI to make those findings easier to understand.

---

#  What Does the Project Do?

The application can:

* Clone a Git repository using **JGit**
* Find Java source files
* Run **PMD** analysis
* Run **SpotBugs** analysis
* Parse the generated reports
* Store the detected issues in an **H2 database**
* Send the detected issues to **Google Gemini**
* Generate a simple explanation of the issue
* Explain the possible impact
* Suggest a fix
* Provide an example of corrected code

---

#  PMD and SpotBugs

### PMD

**PMD (Programming Mistake Detector)** analyzes Java source code and looks for code-quality and maintainability problems. For example, it can identify unused variables, unnecessary code, overly complex methods, and other coding-practice issues.

In this project, PMD generates an XML report, which is then parsed and converted into the application's review-issue format.

### SpotBugs

**SpotBugs** analyzes compiled Java bytecode and looks for patterns that may indicate potential bugs. It can identify issues such as possible null pointer dereferences, incorrect `equals()`/`hashCode()` implementations, and resource-handling problems.

In this project, SpotBugs generates an XML report containing the detected findings, which are then processed by the application.

### Why use both?

They look at code from different perspectives:

| Tool         | Main Focus                       |
| ------------ | -------------------------------- |
| **PMD**      | Code quality and maintainability |
| **SpotBugs** | Potential bugs and correctness   |

Using both gives the project better coverage than relying on only one static-analysis tool.

---

#  How It Works

The overall flow is:

```text
                 Git Repository
                       │
                       ▼
                    JGit
                       │
                 Clone Repository
                       │
                       ▼
                Java Source Files
                       │
              ┌────────┴────────┐
              │                 │
              ▼                 ▼
             PMD            SpotBugs
              │                 │
              ▼                 ▼
          pmd.xml       spotbugsXml.xml
              │                 │
              └────────┬────────┘
                       ▼
                 Parse Findings
                       │
                       ▼
                  Review Issues
                    /       \
                   /         \
                  ▼           ▼
                H2 DB       Gemini
                              │
                              ▼
                    Explanation & Fix
```

---

# 🔄 Step-by-Step Flow

## 1. Repository Submission

The application receives a Git repository URL.

```text
Repository URL
       ↓
Review Request
```

A `ReviewRequest` is created to keep track of the review.

---

## 2. Clone the Repository

The project uses **JGit** to clone the repository into a temporary directory.

This gives the application a local copy of the source code that can be analyzed.

---

## 3. Find Java Files

Once the repository is cloned, the application scans the repository and identifies Java files.

JavaParser can also be used to understand the structure of Java source code, such as:

```text
Class
 ├── Fields
 ├── Constructors
 └── Methods
```

---

## 4. Run PMD

PMD is executed through Maven.

```bash
mvn pmd:pmd
```

It generates:

```text
target/pmd.xml
```

The application reads this XML file and converts the findings into its internal `ReviewIssue` model.

---

## 5. Run SpotBugs

SpotBugs is also executed through Maven.

```bash
mvn spotbugs:spotbugs
```

The generated report is:

```text
target/spotbugsXml.xml
```

The application parses this report and extracts information such as:

* Rule
* File
* Line number
* Severity
* Message
* Category

---

# 🤖 Where Does AI Come In?

This is the part that makes the project different from a normal static-analysis tool.

PMD and SpotBugs already tell us **what is wrong**.

For example:

```text
Tool: SpotBugs

Rule:
NP_NULL_ON_SOME_PATH

File:
UserService.java

Line:
42

Message:
Possible null pointer dereference
```

Instead of sending the entire repository to Gemini, the application sends the **specific detected issue**.

Gemini can then provide something like:

```text
Explanation:
The variable may be null on one execution path.

Impact:
This could result in a NullPointerException at runtime.

Suggested Fix:
Check whether the variable is null before accessing it.

Example:
if (user != null) {
    user.getName();
}
```

This keeps the AI part focused on the actual findings instead of asking the model to review the entire codebase from scratch.

---

# 🧠 Why Not Ask Gemini to Review Everything?

That was an important design decision in this project.

A complete repository can contain a large amount of code. Sending everything to an LLM can:

* Increase token usage
* Increase processing time
* Increase cost
* Make the AI review less focused
* Produce inconsistent results

Instead, the project follows:

```text
PMD / SpotBugs
       ↓
Find actual issues
       ↓
Send only the issue
       ↓
Gemini explains it
```

So the static-analysis tools are responsible for **finding**, while Gemini is responsible for **explaining**.

---

# 🗃️ Database

The project currently uses **H2** to keep the setup simple.

There are two important entities.

### ReviewRequest

Represents one code-review request.

```text
ReviewRequest
-------------------------
id
repositoryUrl
status
createdAt
completedAt
```

### ReviewIssueEntity

Represents an individual issue found during the review.

```text
ReviewIssueEntity
-------------------------
id
tool
rule
fileName
lineNumber
severity
category
message
reviewRequest
```

The relationship is:

```text
ReviewRequest
      │
      │ 1
      │
      │ *
      ▼
ReviewIssueEntity
```

So one review request can have multiple issues.

---

# 🛠️ Tech Stack

### Backend

* Java 21
* Spring Boot
* Spring Framework
* Spring AI
* Maven

### Code Analysis

* PMD
* SpotBugs
* JavaParser

### AI

* Google Gemini
* Spring AI `ChatClient`

### Git

* JGit

### Database

* H2

---

# 📂 Project Structure

The project is organized into separate layers so that each part has a clear responsibility.

```text
src/main/java/com/popita/codereviewagent
│
├── controller
│
├── service
│
├── repository
│
├── entity
│   ├── ReviewRequest
│   └── ReviewIssueEntity
│
├── model
│   ├── ReviewIssue
│   └── RepositoryAnalysis
│
├── analyzer
│
├── rule
│   ├── CodeReviewRule
│   └── SystemOutRule
│
└── ...
```

The exact structure may change as the project evolves.

---

#  Configuration

The application uses a Gemini API key.

For example:

```properties
spring.ai.google.genai.api-key=${GEMINI_API_KEY}
```

The API key should be provided through an environment variable rather than being committed to Git.

### Windows

```powershell
$env:GEMINI_API_KEY="your-api-key"
```

### Linux/macOS

```bash
export GEMINI_API_KEY="your-api-key"
```

---

#  Running the Project

### 1. Clone the repository

```bash
git clone <repository-url>
cd codereviewagent
```

### 2. Configure the Gemini API key

Set the `GEMINI_API_KEY` environment variable.

### 3. Build the project

```bash
mvn clean install
```

### 4. Run the application

```bash
mvn spring-boot:run
```

The application runs on:

```text
http://localhost:8080
```

---

#  Running Static Analysis Manually

You can also run the tools directly through Maven.

### PMD

```bash
mvn pmd:pmd
```

Report:

```text
target/pmd.xml
```

### SpotBugs

```bash
mvn spotbugs:spotbugs
```

Report:

```text
target/spotbugsXml.xml
```

---

#  Design Approach

One of the main things I wanted from this project was to keep the different responsibilities separated.

For example:

```text
GitCloneService
       ↓
Repository Analysis
       ↓
PMD / SpotBugs
       ↓
Issue Parsing
       ↓
Review Service
       ↓
AI Explanation
```

This makes it easier to add another analysis tool later without rewriting the complete application.

The project also uses a rule-based approach:

```text
CodeReviewRule
      │
      ├── SystemOutRule
      ├── Future Rule
      └── Future Rule
```

This makes the code-review engine extensible.

---

# Possible Improvements

There are several things I would like to add in the future:

* Analyze only the latest commit
* Analyze only changed files
* Pull Request-based reviews
* GitHub/GitLab integration
* Checkstyle integration
* Web UI for viewing review results
* Review history
* Severity-based filtering
* AI-generated overall review summary
* Automatic Pull Request comments
* PostgreSQL instead of H2
* CI/CD integration
* Docker-based deployment

---

#  What I Learned From This Project

This project helped me understand how different technologies can work together to solve a real developer problem.

Some of the main areas covered are:

* Spring Boot application design
* Service and repository layers
* JGit
* Maven plugins
* PMD
* SpotBugs
* JavaParser
* XML report parsing
* Spring AI
* Gemini integration
* JPA/Hibernate
* Entity relationships
* Rule-based design
* AI-assisted developer tools

---

#  In Short

The project can be summarized in one sentence:

> **PMD and SpotBugs find the issues, and Gemini helps developers understand them.**

The main purpose is to combine the reliability of traditional static analysis with the explanatory capabilities of Generative AI and create a more developer-friendly code review experience.
