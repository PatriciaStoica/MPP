package com.example.MPPproject.Controller;

import org.springframework.web.bind.annotation.CrossOrigin;


import com.example.MPPproject.Model.Task;
import com.example.MPPproject.Service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<Task> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        Map<Long, Task> taskMap = new HashMap<>();

        for (Task task : tasks) {
            if (!taskMap.containsKey(task.getId())) {
                taskMap.put(task.getId(), task);
            }
        }

        List<Task> uniqueTasks = new ArrayList<>(taskMap.values());
        return uniqueTasks;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable long id) {
        Optional<Task> taskOptional = taskService.getTaskById(id);
        return taskOptional.map(task -> ResponseEntity.ok().body(task))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task createdTask = taskService.saveTask(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable long id) {
        boolean deleted = taskService.deleteTask(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable long id, @RequestBody Map<String, String> taskNameMap) {
        String newName = taskNameMap.get("taskName");
        if (newName == null || newName.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Optional<Task> taskOptional = taskService.getTaskById(id);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            task.setTaskName(newName);
            Task updatedTask = taskService.saveTask(task);
            return ResponseEntity.ok().body(updatedTask);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
