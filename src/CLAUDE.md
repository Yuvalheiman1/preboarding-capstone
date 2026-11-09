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
Layered, package-by-feature (everything in `com.example.tasks`):
- **Controller** (`TaskController`): HTTP only. Mapping, `@Valid`, success status. No business logic, no storage.
- **Service** (`TaskService`): business rules. Knows nothing about HTTP; signals errors by throwing (e.g. `TaskNotFoundException`).
- **Repository** (`TaskRepository`): storage only. In-memory for now; to be replaced by Spring Data when SQLite is connected.
- **Errors**: `GlobalExceptionHandler` (`@RestControllerAdvice`) turns exceptions into ProblemDetail responses. Controllers have no error-status code.
- Dependencies are passed in through the constructor (no `@Autowired` fields).

## Conventions
- Classes: PascalCase
- Tests: snake_case
- Controller tests use `@WebMvcTest`, `MockMvc` and `@Import({TaskService.class, TaskRepository.class})`
- Service tests are plain JUnit with no Spring: `new TaskService(new TaskRepository())`
- To add or change an endpoint, use the `add-endpoint` skill (`.claude/skills/add-endpoint/`)
- Prefer static imports for frequently used utility methods, including all MockMvc request builders and result matchers, to keep test code concise and readable.

## Rules
- Work with TDD.
- Don't commit before making sure I understand.
- Commit only when all tests pass and the code is linted and clear.
- Use `rtk` instead of `gh`.
- Prefer readable code over elegant code.

## Current focus
Learning Spring Boot structure and best practices through a simple, classic project.