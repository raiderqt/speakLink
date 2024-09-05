package com.example.SpeakLink.service.impl;

import com.example.SpeakLink.dto.MessageDto;
import com.example.SpeakLink.entity.Message;
import com.example.SpeakLink.entity.User;
import com.example.SpeakLink.repository.MessageRepository;
import com.example.SpeakLink.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.springframework.security.core.Authentication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageServiceImplTest {

    @Mock
    UserRepository userRepository;
    @InjectMocks
    MessageServiceImpl messageService;
    @Mock
    Authentication authentication;
    @Mock
    ModelMapper modelMapper;
    @Mock
    MessageRepository messageRepository;

    @Test
    @DisplayName("тест метода inputUserMessage на состояние ок")
    void inputUserMessage_ok() {
        MessageDto messageDto = new MessageDto();
        messageDto.setText("1112");

        Message message = new Message();
        User user = new User();
        user.setEmail("test@test");

        MessageDto expectedMessageDto = new MessageDto();
        expectedMessageDto.setText("1112");

        when(modelMapper.map(messageDto, Message.class)).thenReturn(message);
        when(modelMapper.map(message, MessageDto.class)).thenReturn(messageDto);

        when(authentication.getName()).thenReturn("test@test");
        when(userRepository.findByEmail("test@test")).thenReturn(user);

        MessageDto result = messageService.inputUserMessage(messageDto, authentication);
        assertEquals(expectedMessageDto.getText(),result.getText());
    }

    @Test
    @DisplayName("тест метода inputUserMessage когда сообщение null")
    void inputUserMessage_not_ok() {
        MessageDto messageDto = new MessageDto();
        messageDto.setText(null);

        Message message = new Message();
        User user = new User();
        user.setEmail("test@test");
        user.setName("Stasik");

        MessageDto expectedMessageDto = new MessageDto();
        expectedMessageDto.setText(null);

        when(modelMapper.map(messageDto, Message.class)).thenReturn(message);
        when(modelMapper.map(message, MessageDto.class)).thenReturn(messageDto);

        when(authentication.getName()).thenReturn("test@test");
        when(userRepository.findByEmail("test@test")).thenReturn(user);

        MessageDto result = messageService.inputUserMessage(messageDto, authentication);

        assertEquals(expectedMessageDto.getText(),result.getText());
    }

    @Test
    @DisplayName("тест метода inputUserMessage когда сообщение пробел")
    void inputUserMessage_space() {

        MessageDto messageDto = new MessageDto();
        messageDto.setText(" ");

        Message message = new Message();
        User user = new User();
        user.setEmail("test@test");
        user.setName("Stasik");

        MessageDto expectedMessageDto = new MessageDto();
        expectedMessageDto.setText(null);

        when(modelMapper.map(messageDto, Message.class)).thenReturn(message);
        when(modelMapper.map(message, MessageDto.class)).thenReturn(messageDto);

        when(authentication.getName()).thenReturn("test@test");
        when(userRepository.findByEmail("test@test")).thenReturn(user);

        MessageDto result = messageService.inputUserMessage(messageDto, authentication);

        assertEquals(null,result.getText());
    }

    @Test
    @DisplayName("тест метода inputUserMessage когда сообщение пустое")
    void inputUserMessage_pusto() {
        MessageDto messageDto = new MessageDto();
        messageDto.setText("");

        Message message = new Message();
        User user = new User();
        user.setEmail("test@test");
        user.setName("Stasik");

        MessageDto expectedMessageDto = new MessageDto();
        expectedMessageDto.setText(null);

        when(modelMapper.map(messageDto, Message.class)).thenReturn(message);
        when(modelMapper.map(message, MessageDto.class)).thenReturn(messageDto);

        when(authentication.getName()).thenReturn("test@test");
        when(userRepository.findByEmail("test@test")).thenReturn(user);

        MessageDto result = messageService.inputUserMessage(messageDto, authentication);

        assertEquals(null, result.getText());
    }
}