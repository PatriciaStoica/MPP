package com.example.mpp_project.Service;

import com.example.mpp_project.Model.User;
import com.example.mpp_project.Model.UserTaskCount;
import com.example.mpp_project.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final TaskService taskService;

    @Autowired
    public UserService(UserRepository userRepository, TaskService taskService) {
        this.userRepository = userRepository;
        this.taskService = taskService;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public boolean deleteUser(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            userRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public List<UserTaskCount> getUsersWithTaskCount(int offset, int pageSize) {
        List<UserTaskCount> userTaskCounts = userRepository.findUsersWithTaskCount(offset, pageSize);

        // Iterate over each user task count and set the task count
        for (UserTaskCount userTaskCount : userTaskCounts) {
            long userId = userTaskCount.getUser().getId();
            long taskCount = taskService.getTaskCountByUserId(userId);
            userTaskCount.setTaskCount(taskCount);
        }

        return userTaskCounts;
    }

}
