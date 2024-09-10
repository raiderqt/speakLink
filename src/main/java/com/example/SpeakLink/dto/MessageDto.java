package com.example.SpeakLink.dto;

import lombok.*;

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
