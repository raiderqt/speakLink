package com.example.LearningDevProject1.dto;

import com.example.LearningDevProject1.entity.Room;
import com.example.LearningDevProject1.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomDto
{
	private UUID id;
	private String name;
	private String info;
	private String type;
	private List<UserDto> users;
}
