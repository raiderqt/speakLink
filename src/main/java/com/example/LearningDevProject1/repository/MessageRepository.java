package com.example.LearningDevProject1.repository;

import com.example.LearningDevProject1.entity.Message;
import com.example.LearningDevProject1.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface MessageRepository extends JpaRepository<Message, Long>
{
	List<Message> findAllByRoomOrderByTimestamp(Room room);
}