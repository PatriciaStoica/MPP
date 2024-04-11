package com.example.MPPproject.Repository;

import com.example.MPPproject.Model.Task;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TaskRepository {

    private final List<Task> taskList = new ArrayList<>();
    private long taskIdCounter = 1;

    public List<Task> findAll() {
        return new ArrayList<>(taskList);
    }

    public Optional<Task> findById(long id) {
        return taskList.stream().filter(task -> task.getId() == id).findFirst();
    }

    public Task save(Task task) {
        if (task.getId() == 0) {
            task.setId(taskIdCounter++);
        }
        taskList.add(task);
        return task;
    }

    public boolean deleteById(long id) {
        return taskList.removeIf(task -> task.getId() == id);
    }
}

