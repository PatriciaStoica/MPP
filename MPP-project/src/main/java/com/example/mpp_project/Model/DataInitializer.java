package com.example.mpp_project.Model;

import com.example.mpp_project.Service.TaskService;
import com.example.mpp_project.Service.UserService;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    private final UserService userService;
    private final TaskService taskService;

    @Autowired
    public DataInitializer(UserService userService, TaskService taskService) {
        this.userService = userService;
        this.taskService = taskService;
    }

    public void populateDatabaseWithFakeData() {
        //populateUsers();
        populateTasks();
    }

    /*private void populateUsers() {
        for (int i = 0; i < 1000; i++) {
            User user = new User();
            user.setUserName(Faker.instance().name().username());
            userService.saveUser(user);
        }
    }*/

    private void populateTasks() {
        for (int i = 0; i < 1000; i++) {
            Task task = new Task();
            task.setTaskName(Faker.instance().funnyName().name());
            task.setUserId((long)(Math.random()*1000)); // Assign a random user ID
            taskService.saveTask(task);
        }
    }
}
