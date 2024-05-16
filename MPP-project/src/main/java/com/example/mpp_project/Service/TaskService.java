package com.example.mpp_project.Service;

import com.example.mpp_project.Model.Task;
import com.example.mpp_project.Repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public List<Task> getAllTasks(String sortBy, String sortOrder) {
        if (sortBy == null || sortBy.isEmpty() || sortOrder == null || sortOrder.isEmpty()) {
            throw new IllegalArgumentException("SortBy and sortOrder must not be null or empty");
        }
        Sort.Direction direction = sortOrder.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        return taskRepository.findAll(Sort.by(direction, sortBy));
    }

    /*public List<Task> getAllTasks(String sortBy, String sortOrder, int page, int pageSize) {
        if (sortBy == null || sortBy.isEmpty() || sortOrder == null || sortOrder.isEmpty()) {
            throw new IllegalArgumentException("SortBy and sortOrder must not be null or empty");
        }

        Sort.Direction direction = sortOrder.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page - 1, pageSize, direction, sortBy);
        Page<Task> taskPage = taskRepository.findAll(pageable);
        return taskPage.getContent();
    }*/

    public List<Task> getAllTasks(String sortBy, String sortOrder, int page, int pageSize) {
        // Calculate offset based on page number and page size
        int offset = (page - 1) * pageSize;
        return taskRepository.findAll(PageRequest.of(offset, pageSize, Sort.by(Sort.Direction.fromString(sortOrder), sortBy))).getContent();
    }


    public Optional<Task> getTaskById(long id) {
        return taskRepository.findById(id);
    }

    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }

    public boolean deleteTask(long id) {
        Optional<Task> taskOptional = taskRepository.findById(id);
        if (taskOptional.isPresent()) {
            taskRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public long getTaskCountByUserId(Long userId) {
        return taskRepository.countByUserId(userId);
    }
}
