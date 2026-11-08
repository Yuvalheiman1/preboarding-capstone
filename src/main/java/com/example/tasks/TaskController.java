package com.example.tasks;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
public class TaskController {

    private final AtomicLong counter = new AtomicLong();
    private final Map<Long, Task> tasks = new ConcurrentHashMap<>();

    @GetMapping("/tasks")
    public Collection<Task> listTasks() {
        return tasks.values();
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<Task> getTask(
            @PathVariable Long id) {
        Task task = tasks.get(id);
        if (task != null) {
            return ResponseEntity.ok().body(task);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/tasks")
    public ResponseEntity<Task> createTask(
            @Valid @RequestBody CreateTaskRequest request) {
        Task newTask = new Task(counter.incrementAndGet(),
                request.title(),
                Boolean.TRUE.equals(request.done()));   //null -> false
        tasks.put(newTask.id(), newTask);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTask);
    }

    @PutMapping("/tasks/{id}")
    public ResponseEntity<Task> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTaskRequest request) {
        Task updatedTask = new Task(id, request.title(), request.done());
        // replace() writes only if the id already exists, and returns null if it doesn't
        Task oldTask = tasks.replace(id, updatedTask);
        if (oldTask != null) {
            return ResponseEntity.ok().body(updatedTask);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long id) {
        Task task = tasks.remove(id);
        if (task != null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
