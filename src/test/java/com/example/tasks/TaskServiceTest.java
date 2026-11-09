package com.example.tasks;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

// Plain JUnit: no Spring, the objects are created by hand.
class TaskServiceTest {

    private final TaskService taskService = new TaskService(new TaskRepository());

    @Test
    void count_tasks_counts_total_and_done() {
        taskService.createTask(new CreateTaskRequest("Task 1", true));
        taskService.createTask(new CreateTaskRequest("Task 2", false));
        taskService.createTask(new CreateTaskRequest("Task 3", true));

        TaskCountResponse count = taskService.countTasks();

        assertEquals(3, count.total());
        assertEquals(2, count.done());
    }
}
