package com.example.LearningDevProject1.service;

import com.example.LearningDevProject1.dto.MessageDto;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.UUID;
/**
 * Сервис обработки сообщений пользователей.
 */
public interface MessageService {
	List<MessageDto> allRoomMessage(UUID roomDto, Authentication authentication);

	MessageDto inputUserMessage(MessageDto messageDto, Authentication authentication);
}
