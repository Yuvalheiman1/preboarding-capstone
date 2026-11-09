package com.example.tasks;

public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(long id) {
        super("Task " + id + " not found");
    }
}
