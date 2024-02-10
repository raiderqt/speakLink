package com.example.LearningDevProject1.service.impl;

import com.example.LearningDevProject1.dto.MessageDto;
import com.example.LearningDevProject1.dto.RoomDto;
import com.example.LearningDevProject1.entity.Message;
import com.example.LearningDevProject1.entity.Room;
import com.example.LearningDevProject1.entity.User;
import com.example.LearningDevProject1.repository.MessageRepository;
import com.example.LearningDevProject1.repository.RoomRepository;
import com.example.LearningDevProject1.repository.UserRepository;
import com.example.LearningDevProject1.service.MessageService;
import com.example.LearningDevProject1.service.RoomService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Service
public class MessageServiceImpl implements MessageService
{
	private final ModelMapper modelMapper;
	private final MessageRepository messageRepository;
	private final UserRepository userRepository;
	private final RoomRepository roomRepository;

	public MessageServiceImpl(UserRepository userRepository,
							  MessageRepository messageRepository,
							  ModelMapper modelMapper,
							  RoomRepository roomRepository)
	{
		this.messageRepository = messageRepository;
		this.modelMapper = modelMapper;
		this.userRepository = userRepository;
		this.roomRepository = roomRepository;
	}

	public MessageDto inputUserMessage(MessageDto messageDto, Authentication authentication)
	{
		User user = userRepository.findByEmail(authentication.getName());
		Message message = modelMapper.map(messageDto, Message.class);
		message.setUser(user);
		message.setTimestamp(Timestamp.from(Instant.now()));
		messageRepository.save(message);
		return modelMapper.map(message, MessageDto.class);
	}

	@Override
	public List<MessageDto> allRoomMessage(UUID roomDto, Authentication authentication) {
		User user = userRepository.findByEmail(authentication.getName());
		Optional<Room> room = roomRepository
				.findByIdAndUsers(roomDto, user);
		List<MessageDto> result = new ArrayList<>();
		room.ifPresent(currentRoom -> {
			List<Message> list = messageRepository.findAllByRoomOrderByTimestamp(currentRoom);
			List<MessageDto> listDto = list.stream().map(message ->
					{
						MessageDto dto = modelMapper.map(message, MessageDto.class);
						dto.setRoom(null);
						return dto;
					})
					.toList();
			result.addAll(listDto);
		});

		return result;
	}
}