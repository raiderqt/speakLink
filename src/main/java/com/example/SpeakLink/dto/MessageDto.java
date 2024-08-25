package com.example.SpeakLink.dto;

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
