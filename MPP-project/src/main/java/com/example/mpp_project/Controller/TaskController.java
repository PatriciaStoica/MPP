package com.example.mpp_project.Controller;

import com.example.mpp_project.Model.Task;
import com.example.mpp_project.Service.UserService;
import org.springframework.web.bind.annotation.CrossOrigin;


import com.example.mpp_project.Service.TaskService;
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
    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
    }


    /*@GetMapping
    public List<Task> getAllTasks(@RequestParam(required = false) String sortBy, @RequestParam(required = false, defaultValue = "ASC") String sortOrder) {
        if (sortBy != null) {
            return taskService.getAllTasks(sortBy, sortOrder);
        } else {
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
    }*/

    /*@GetMapping
    public List<Task> getAllTasks(
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "ASC") String sortOrder,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "50") int pageSize
    ) {
        if (sortBy != null) {
            return taskService.getAllTasks(sortBy, sortOrder, page, pageSize);
        } else {
            List<Task> tasks = taskService.getAllTasks("taskName","ASC", page, pageSize);
            Map<Long, Task> taskMap = new HashMap<>();

            for (Task task : tasks) {
                if (!taskMap.containsKey(task.getId())) {
                    taskMap.put(task.getId(), task);
                }
            }

            List<Task> uniqueTasks = new ArrayList<>(taskMap.values());
            return uniqueTasks;
        }
    }*/

    @GetMapping
    public List<Task> getAllTasks(
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "ASC") String sortOrder,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "50") int pageSize
    ) {
        return taskService.getAllTasks(sortBy, sortOrder, page, pageSize);
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

    @PatchMapping("/{id}/user")
    public ResponseEntity<Task> updateTaskUser(@PathVariable long id, @RequestBody Map<String, Long> userIdMap) {
        Long newUserId = userIdMap.get("userId");
        if (newUserId == null) {
            return ResponseEntity.badRequest().build();
        }

        Optional<Task> taskOptional = taskService.getTaskById(id);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            task.setUserId(newUserId);
            Task updatedTask = taskService.saveTask(task);
            return ResponseEntity.ok().body(updatedTask);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
