package com.example.SpeakLink.repository;

import com.example.SpeakLink.entity.Room;
import com.example.SpeakLink.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    List<User> findAllByRooms(Room room);
}
