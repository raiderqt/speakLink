package com.example.SpeakLink.repository;

import com.example.SpeakLink.entity.Message;
import com.example.SpeakLink.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface MessageRepository extends JpaRepository<Message, Long>
{
	List<Message> findAllByRoomOrderByTimestamp(Room room);
}