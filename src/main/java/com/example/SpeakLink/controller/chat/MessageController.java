package com.example.SpeakLink.controller.chat;

import com.example.SpeakLink.dto.MessageDto;
import com.example.SpeakLink.dto.RoomDto;
import com.example.SpeakLink.service.MessageService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.UUID;
@Controller
public class MessageController
{
	private final MessageService messageService;

	public MessageController(MessageService messageService) {
		this.messageService = messageService;
	}

	@MessageMapping("/message/{roomId}")
	@SendTo("/room/{roomId}")
	public List<MessageDto> inputMessage(@Payload MessageDto message, Authentication authentication, @DestinationVariable("roomId") String roomId)
	{
		RoomDto roomDto = new RoomDto();
		roomDto.setId(UUID.fromString(roomId));
		message.setRoom(roomDto);
		return List.of(messageService.inputUserMessage(message, authentication));
	}

	@PostMapping("/message")
	@ResponseBody
	public List<MessageDto> getAllRoomMessages(@RequestParam("roomId") String roomId, Authentication authentication)
	{
		return messageService.allRoomMessage(UUID.fromString(roomId), authentication);
	}
}
