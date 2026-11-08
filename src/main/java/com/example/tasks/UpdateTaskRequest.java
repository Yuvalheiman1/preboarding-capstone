package com.example.tasks;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

public record UpdateTaskRequest(
        @NotBlank
        String title,
        @NotNull
        Boolean done,
        // The id comes from the URL only. This field exists so that sending one is rejected with 400.
        @Null
        Long id) {}
