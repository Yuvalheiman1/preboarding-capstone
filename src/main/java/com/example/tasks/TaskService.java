package com.example.tasks;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class TaskService {

    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public List<Task> listTasks(Boolean done) {
        List<Task> allTasks = repository.findAll();
        if (done == null) {
            return allTasks;
        }
        return allTasks.stream()
                .filter(task -> task.done() == done)
                .toList();
    }

    public Task getTask(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    public Task createTask(CreateTaskRequest request) {
        boolean done = Boolean.TRUE.equals(request.done());   // null -> false
        return repository.insert(request.title(), done);
    }

    public Task updateTask(long id, UpdateTaskRequest request) {
        Task updatedTask = new Task(id, request.title(), request.done());
        return repository.replace(updatedTask)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    public TaskCountResponse countTasks() {
        // One snapshot, so total and done are counted from the same moment
        List<Task> allTasks = repository.findAll();
        long doneCount = allTasks.stream()
                .filter(task -> task.done())
                .count();
        return new TaskCountResponse(allTasks.size(), doneCount);
    }

    public void deleteTask(long id) {
        boolean deleted = repository.deleteById(id);
        if (!deleted) {
            throw new TaskNotFoundException(id);
        }
    }
}
