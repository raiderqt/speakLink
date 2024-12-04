package com.example.SpeakLink.controller.chat;

import com.example.SpeakLink.dto.RoomDto;
import com.example.SpeakLink.dto.UserDto;
import com.example.SpeakLink.entity.Room;
import com.example.SpeakLink.service.RoomService;
import com.example.SpeakLink.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;
import java.util.Set;

@Controller
public class RoomController {
	private final UserService userService;
	private final RoomService roomService;
	private final ObjectMapper objectMapper;

	@Autowired
	public RoomController(UserService userService, RoomService roomService, ObjectMapper objectMapper) {
		this.userService = userService;
		this.roomService = roomService;
		this.objectMapper = objectMapper;
	}

	@PostMapping("/chat/rooms")
	@ResponseBody
	public Set<RoomDto> getRoomList(Authentication authentication) {
		return roomService.getRoomList(authentication);
	}

}
