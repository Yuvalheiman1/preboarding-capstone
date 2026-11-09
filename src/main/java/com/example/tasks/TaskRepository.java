package com.example.tasks;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

// In-memory storage. Will be replaced by a Spring Data repository when SQLite is connected.
@Repository
public class TaskRepository {

    private final AtomicLong counter = new AtomicLong();
    private final Map<Long, Task> tasks = new ConcurrentHashMap<>();

    public List<Task> findAll() {
        return List.copyOf(tasks.values());
    }

    public Optional<Task> findById(long id) {
        return Optional.ofNullable(tasks.get(id));
    }

    // Gives out the next id, like an auto-increment column in a database
    public Task insert(String title, boolean done) {
        Task task = new Task(counter.incrementAndGet(), title, done);
        tasks.put(task.id(), task);
        return task;
    }

    // Saves only if the id already exists (atomic). Empty if it doesn't.
    public Optional<Task> replace(Task task) {
        Task oldTask = tasks.replace(task.id(), task);
        if (oldTask == null) {
            return Optional.empty();
        }
        return Optional.of(task);
    }

    public boolean deleteById(long id) {
        return tasks.remove(id) != null;
    }
}
