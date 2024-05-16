package com.example.mpp_project.Repository;

import com.example.mpp_project.Model.User;
import com.example.mpp_project.Model.UserTaskCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT new com.example.mpp_project.Model.UserTaskCount(u, COUNT(t)) " +
            "FROM User u LEFT JOIN Task t ON u.id = t.userId " +
            "GROUP BY u.id")
    List<UserTaskCount> findUsersWithTaskCount(int offset, int pageSize);
}

