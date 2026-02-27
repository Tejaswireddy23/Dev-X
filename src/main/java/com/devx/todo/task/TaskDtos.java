package com.devx.todo.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.List;

public class TaskDtos {

    public record TaskRequest(
            @NotBlank @Size(max = 120) String title,
            @Size(max = 60) String category,
            TaskStatus status
    ) {
    }

    public record TaskResponse(
            Long id,
            String title,
            String category,
            TaskStatus status,
            Instant createdAt,
            Instant updatedAt
    ) {
    }

    public record SuggestRequest(@NotBlank @Size(max = 200) String prompt) {
    }

    public record SuggestResponse(List<String> suggestions) {
    }
}
