package com.devx.todo.task;

import com.devx.todo.config.AuthenticatedUser;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<TaskDtos.TaskResponse>> getAll(@AuthenticationPrincipal AuthenticatedUser user) {
        return ResponseEntity.ok(taskService.getAll(user));
    }

    @PostMapping
    public ResponseEntity<TaskDtos.TaskResponse> create(
            @AuthenticationPrincipal AuthenticatedUser user,
            @Valid @RequestBody TaskDtos.TaskRequest request) {
        return ResponseEntity.ok(taskService.create(user, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDtos.TaskResponse> update(
            @AuthenticationPrincipal AuthenticatedUser user,
            @PathVariable Long id,
            @Valid @RequestBody TaskDtos.TaskRequest request) {
        return ResponseEntity.ok(taskService.update(user, id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal AuthenticatedUser user, @PathVariable Long id) {
        taskService.delete(user, id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/suggest")
    public ResponseEntity<TaskDtos.SuggestResponse> suggest(
            @AuthenticationPrincipal AuthenticatedUser user,
            @Valid @RequestBody TaskDtos.SuggestRequest request) {
        return ResponseEntity.ok(taskService.suggest(user, request));
    }
}
