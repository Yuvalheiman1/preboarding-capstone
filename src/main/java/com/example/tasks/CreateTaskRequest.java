package com.example.tasks;

import jakarta.validation.constraints.NotBlank;

public record CreateTaskRequest(
        @NotBlank
        String title,
        Boolean done) {}
