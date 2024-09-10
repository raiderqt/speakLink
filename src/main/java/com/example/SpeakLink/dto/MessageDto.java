package com.example.SpeakLink.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;
import java.sql.Timestamp;

@Builder
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
