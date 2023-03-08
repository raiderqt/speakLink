package com.example.LearningDevProject1.controller.chat;

import com.example.LearningDevProject1.dto.RoomDto;
import com.example.LearningDevProject1.service.RoomService;
import com.example.LearningDevProject1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
@Controller
public class RoomController
{
	private final UserService userService;
	private final RoomService roomService;

	@Autowired
	public RoomController(UserService userService, RoomService roomService)
	{
		this.userService = userService;
		this.roomService = roomService;
	}


	@PostMapping("/chat/rooms")
	@ResponseBody
	public List<RoomDto> getRoomList(Authentication authentication)
	{
		return roomService.getRoomList(authentication);
	}
}
