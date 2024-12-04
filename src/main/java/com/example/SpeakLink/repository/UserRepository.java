package com.example.SpeakLink.repository;

import com.example.SpeakLink.entity.Room;
import com.example.SpeakLink.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    Optional<User> findById(Long id);

    Set<User> findAllByRooms(Room room);

    List<User> findAllByNameStartingWith(String name);
}
