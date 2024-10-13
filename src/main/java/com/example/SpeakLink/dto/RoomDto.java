package com.example.SpeakLink.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;
@Builder
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
