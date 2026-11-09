---
name: add-endpoint
description: Adds a new REST endpoint to this Spring Boot project with tests. Use when asked to add or change an API endpoint, e.g. "add GET /tasks/count", "support filtering by X", "add a PATCH", even if the word "endpoint" isn't used.
---

# Add an endpoint

1. **Read first.** Read [learnings.md](learnings.md), `src/CLAUDE.md`, the existing controller, service, repository and tests, so the new code matches their conventions.
2. **Test first.** Write a MockMvc test for the new endpoint, including error cases (400, 404, invalid params) if the endpoint has any. If it has none, say so.
   - Put business logic tests in `TaskServiceTest` (plain JUnit, no Spring).
   - Run the tests and confirm they fail because the feature is missing.
3. **Implement.** Keep business logic out of the controller.
   - Controller: HTTP only. Service: the rules. Repository: storage.
4. **Run the tests.** Run `.\mvnw.cmd test` (Windows) and fix failures until all pass.
5. **Summarize.** Describe the change and list the files you touched.
6. **Ask about decisions.** If there are important decisions that need Yuval's attention, ask instead of choosing.
7. **Log learnings.** After using this skill, add what you learned to [learnings.md](learnings.md).
   - If a learning suggests changing this file, propose the change and edit it only with Yuval's permission.
   - If `learnings.md` goes over 20 KB, compress it: merge duplicate entries and shorten older ones into one-line rules.
