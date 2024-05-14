package com.example.mpp_project.cronjob;

import com.example.mpp_project.Model.Task;
import com.example.mpp_project.Service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class WebSocketConfigCronjob {

    private final SimpMessagingTemplate messagingTemplate;
    private final TaskService taskService;

    @Autowired
    public WebSocketConfigCronjob(SimpMessagingTemplate messagingTemplate, TaskService taskService) {
        this.messagingTemplate = messagingTemplate;
        this.taskService = taskService;
    }

    @Scheduled(fixedRate = 10000)
    public void createEntityAndSendViaWebSocket() {
        try {
            Task newTask = new Task("New Task", false);
            Task createdTask = taskService.saveTask(newTask);

            messagingTemplate.convertAndSend("/topic/newTask", createdTask);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

