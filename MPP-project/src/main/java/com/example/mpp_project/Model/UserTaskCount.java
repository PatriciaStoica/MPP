package com.example.mpp_project.Model;

public class UserTaskCount {
    private User user;
    private long taskCount;

    public UserTaskCount(User user, long taskCount) {
        this.user = user;
        this.taskCount = taskCount;
    }

    // Getters and setters
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(long taskCount) {
        this.taskCount = taskCount;
    }
}
