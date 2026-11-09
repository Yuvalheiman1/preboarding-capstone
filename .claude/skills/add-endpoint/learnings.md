# add-endpoint: Learnings

Newest at the bottom. If this file goes over 20 KB, compress it: merge duplicate entries and shorten older ones into one-line rules.

## 2026-11-08: PUT /tasks/{id} and GET /tasks?done=
- Yuval chose **strict PUT**: every field is required, and a missing field gives 400. "Send only what changes" is PATCH.
- A test that only checks the response missed a data bug: `Map.put` quietly created the task while still returning 404. After a failed request, check that the data didn't change.
- `ConcurrentHashMap` doesn't guarantee iteration order. Use `containsInAnyOrder`, not `$[0]`.
- Red tests for a missing method give **405** (the path exists, the method doesn't), not 404.
- Spring converts path variables and query params by itself: `/tasks/abc` and `?done=maybe` give 400 with no code.
- `@Valid` runs before the method body, so an invalid body sent to a missing id gives 400, not 404.
- A commit message must describe only what that commit contains. Check `git show --stat` first.

## 2026-11-09: refactor to controller → service → repository
- Missing resources: the service throws `TaskNotFoundException`, and `GlobalExceptionHandler` returns a ProblemDetail 404. Yuval chose this over returning `Optional`.
- `@WebMvcTest` doesn't load `@Service` or `@Repository` classes. `ControllerTest` needs `@Import({TaskService.class, TaskRepository.class})`.
- `@ResponseStatus` is only needed when success isn't 200 (201 for create, 204 for delete).

## 2026-11-09: GET /tasks/count (first real use of this skill)
- Yuval chose `record TaskCountResponse(long total, long done)` (the `...Response` name matches `...Request`), and no `?done=` filter (YAGNI).
- A fixed path like `/tasks/count` beats `/tasks/{id}`. Before it exists, the request matches `{id}`, and `"count"` → `Long` fails with **400**. That's the expected red reason.
- Java tests must compile before they can fail. Add a skeleton first (the record, plus a method that throws `UnsupportedOperationException`), so the red is a real test failure, not a compile error.
- Not every endpoint has error cases (no id, no body, no params). Say so instead of inventing tests.
- `ControllerTest` with `@Import` runs all 3 layers, so it overlaps with `TaskServiceTest`. That's accepted for now. The next step would be a fake service (`@MockitoBean`) in controller tests.
- Read-only aggregates: count from one `findAll()` snapshot, so the numbers come from the same moment.
