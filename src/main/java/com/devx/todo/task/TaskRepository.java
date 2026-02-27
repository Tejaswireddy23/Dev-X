package com.devx.todo.task;

import com.devx.todo.auth.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByUserOrderByUpdatedAtDesc(User user);

    Optional<Task> findByIdAndUser(Long id, User user);
}
