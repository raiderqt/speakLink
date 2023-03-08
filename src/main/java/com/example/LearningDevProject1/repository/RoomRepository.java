package com.example.LearningDevProject1.repository;

import com.example.LearningDevProject1.entity.Room;
import com.example.LearningDevProject1.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
public interface RoomRepository extends JpaRepository<Room, UUID>
{
	Room findByName(String name);
	List<Room> findByUsers(User user);

	Optional<Room> findByIdAndUsers(UUID id, User user);
}
