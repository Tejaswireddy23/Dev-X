package com.devx.todo.task;

import com.devx.todo.auth.User;
import com.devx.todo.auth.UserRepository;
import com.devx.todo.config.AuthenticatedUser;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<TaskDtos.TaskResponse> getAll(AuthenticatedUser authenticatedUser) {
        User user = getUser(authenticatedUser);
        return taskRepository.findAllByUserOrderByUpdatedAtDesc(user)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public TaskDtos.TaskResponse create(AuthenticatedUser authenticatedUser, TaskDtos.TaskRequest request) {
        User user = getUser(authenticatedUser);
        Task task = new Task();
        task.setUser(user);
        task.setTitle(request.title().trim());
        task.setCategory(normalizeCategory(request.category()));
        task.setStatus(request.status() == null ? TaskStatus.PENDING : request.status());
        return toResponse(taskRepository.save(task));
    }

    @Transactional
    public TaskDtos.TaskResponse update(AuthenticatedUser authenticatedUser, Long id, TaskDtos.TaskRequest request) {
        User user = getUser(authenticatedUser);
        Task task = taskRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        task.setTitle(request.title().trim());
        task.setCategory(normalizeCategory(request.category()));
        task.setStatus(request.status() == null ? task.getStatus() : request.status());
        return toResponse(taskRepository.save(task));
    }

    @Transactional
    public void delete(AuthenticatedUser authenticatedUser, Long id) {
        User user = getUser(authenticatedUser);
        Task task = taskRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        taskRepository.delete(task);
    }

    @Transactional(readOnly = true)
    public TaskDtos.SuggestResponse suggest(AuthenticatedUser authenticatedUser, TaskDtos.SuggestRequest request) {
        List<TaskDtos.TaskResponse> tasks = getAll(authenticatedUser);
        long pendingCount = tasks.stream().filter(t -> t.status() == TaskStatus.PENDING).count();

        List<String> suggestions = new ArrayList<>();
        String prompt = request.prompt().toLowerCase();
        if (prompt.contains("health") || prompt.contains("workout")) {
            suggestions.add("Add a 20-minute walk to your day.");
            suggestions.add("Plan a simple healthy meal for tonight.");
        }
        if (prompt.contains("study") || prompt.contains("learn")) {
            suggestions.add("Break learning into 25-minute Pomodoro sessions.");
            suggestions.add("Write down 3 key takeaways after each session.");
        }
        if (pendingCount > 5) {
            suggestions.add("You have many pending tasks; pick top 3 by impact and schedule those first.");
        }
        if (suggestions.isEmpty()) {
            suggestions.add("Split your request into one small actionable task.");
            suggestions.add("Set a clear deadline and priority for the new task.");
        }

        return new TaskDtos.SuggestResponse(suggestions);
    }

    private User getUser(AuthenticatedUser authenticatedUser) {
        return userRepository.findById(authenticatedUser.id())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    private String normalizeCategory(String category) {
        if (category == null || category.isBlank()) {
            return "General";
        }
        return category.trim();
    }

    private TaskDtos.TaskResponse toResponse(Task task) {
        return new TaskDtos.TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getCategory(),
                task.getStatus(),
                task.getCreatedAt(),
                task.getUpdatedAt());
    }
}
