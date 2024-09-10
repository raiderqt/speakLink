package com.example.SpeakLink.service.impl;

import com.example.SpeakLink.dto.MessageDto;
import com.example.SpeakLink.entity.Message;
import com.example.SpeakLink.entity.Room;
import com.example.SpeakLink.entity.User;
import com.example.SpeakLink.repository.MessageRepository;
import com.example.SpeakLink.repository.RoomRepository;
import com.example.SpeakLink.repository.UserRepository;
import com.example.SpeakLink.service.MessageService;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class MessageServiceImpl implements MessageService {
    private final ModelMapper modelMapper;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    public MessageServiceImpl(UserRepository userRepository,
                              MessageRepository messageRepository,
                              ModelMapper modelMapper,
                              RoomRepository roomRepository) {
        this.messageRepository = messageRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
    }

    /**
     * добавляет сообщение пользователю
     * @param messageDto - сообщение
     * @param authentication - залогиненый пользователь
     * @return messageDto
     */
    public MessageDto inputUserMessage(MessageDto messageDto, Authentication authentication) {
        if (messageDto != null) {
            User user = userRepository.findByEmail(authentication.getName());
            Message message = modelMapper.map(messageDto, Message.class);
            message.setUser(user);
            message.setTimestamp(Timestamp.from(Instant.now()));

            if (messageDto.getText() == null || messageDto.getText().trim().isEmpty() || messageDto.getText().equals("")) {
                messageDto.setText(null);

            }
            messageRepository.save(message);
            return modelMapper.map(message, MessageDto.class);
        }
        return null;
    }

    /**
     * Вывод сообщений комнаты
     * @param roomId - ID Комнаты
     * @param authentication - залогиненый пользователь
     * @return - лист сообщений
     */
    @Override
    public List<MessageDto> allRoomMessage(UUID roomId, Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName());
        Optional<Room> room = roomRepository
                .findByIdAndUsers(roomId, user);
        List<MessageDto> result = new ArrayList<>();
        room.ifPresent(currentRoom -> {
            List<Message> list = messageRepository.findAllByRoomOrderByTimestamp(currentRoom);
            List<MessageDto> listDto = list.stream().map(message -> {
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