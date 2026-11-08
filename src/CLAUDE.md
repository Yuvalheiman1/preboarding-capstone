# GongPreOnboarding

## What it is
A pre-onboarding course to learn Claude Code and Spring Boot best practices.
The goal is learning and becoming a better engineer before the real onboarding.

## Stack
- Java, Spring Boot
- SQLite (not connected yet)
- AWS (not connected yet)

## Commands
- Run: `.\mvnw.cmd spring-boot:run`
- Test: `.\mvnw.cmd test`

## Structure
- Temporary, for learning: controllers handle both HTTP and logic.

## Conventions
- Classes: PascalCase
- Tests: snake_case
- Controller tests use `@WebMvcTest` and `MockMvc`
- Prefer static imports for frequently used utility methods, including all MockMvc request builders and result matchers, to keep test code concise and readable.

## Rules
- Work with TDD.
- Don't commit before making sure I understand.
- Commit only when all tests pass and the code is linted and clear.
- Use `rtk` instead of `gh`.
- Prefer readable code over elegant code.

## Current focus
Learning Spring Boot structure and best practices through a simple, classic project.