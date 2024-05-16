package com.example.mpp_project.Repository;

import com.example.mpp_project.Model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    long countByUserId(Long userId);
}