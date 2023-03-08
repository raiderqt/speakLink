package com.example.LearningDevProject1.repository;

import com.example.LearningDevProject1.entity.Room;
import com.example.LearningDevProject1.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    List<User> findAllByRooms(Room room);
}
