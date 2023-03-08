package com.example.LearningDevProject1.dto;

import com.example.LearningDevProject1.entity.Room;
import com.example.LearningDevProject1.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto
{
	private String text;
	private RoomDto room;
	private UserDto user;
	private Timestamp timestamp;
}
