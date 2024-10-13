package com.example.SpeakLink.service.impl;

import com.example.SpeakLink.dto.MessageDto;
import com.example.SpeakLink.entity.Message;
import com.example.SpeakLink.entity.Room;
import com.example.SpeakLink.entity.User;
import com.example.SpeakLink.repository.MessageRepository;
import com.example.SpeakLink.repository.RoomRepository;
import com.example.SpeakLink.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.springframework.security.core.Authentication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageServiceImplTest {

    @Mock
    UserRepository userRepository;
    @Mock
    RoomRepository roomRepository;
    @InjectMocks
    MessageServiceImpl messageService;
    @Mock
    Authentication authentication;
    @Mock
    ModelMapper modelMapper;
    @Mock
    MessageRepository messageRepository;


    @Test
    @DisplayName("тест метода allRoomMessage на состояние ок")
    void testAllRoomMessage_OK() {
        User user = generateUser();
        Optional<Room> room = Optional.of(generateRoom());
        Message message = new Message(room.get(), user, "test");
        List<Message> messages = List.of(message);
        MessageDto messageDto = generateMessageDto();

        when(userRepository.findByEmail("test@test")).thenReturn(user);
        when(roomRepository.findByIdAndUsers(room.get().getId(), user)).thenReturn(room);
        when(authentication.getName()).thenReturn("test@test");
        when(messageRepository.findAllByRoomOrderByTimestamp(room.get())).thenReturn(messages);
        when(modelMapper.map(message, MessageDto.class)).thenReturn(messageDto);

        List<MessageDto> expectedRoomMessage = List.of(messageDto);
        List<MessageDto> result = messageService.allRoomMessage(room.get().getId(), authentication);

        assertEquals(expectedRoomMessage, result);
    }

    @Test
    @DisplayName("тест метода allRoomMessage на состояние not ок")
    void testAllRoomMessage_not_OK() {
        User user = generateUser();
        Optional<Room> room = Optional.of(generateRoom());

        Message message = new Message(room.get(), user, "test");
        List<Message> messages = List.of(message);

        MessageDto messageDto = generateMessageDto();
        messageDto.setText(null);

        when(userRepository.findByEmail("test@test")).thenReturn(user);
        when(roomRepository.findByIdAndUsers(room.get().getId(), user)).thenReturn(room);
        when(authentication.getName()).thenReturn("test@test");
        when(messageRepository.findAllByRoomOrderByTimestamp(room.get())).thenReturn(messages);
        when(modelMapper.map(message, MessageDto.class)).thenReturn(messageDto);
        List<MessageDto> expectedRoomMessage = List.of(messageDto);

        List<MessageDto> result = messageService.allRoomMessage(room.get().getId(), authentication);

        assertEquals(result, expectedRoomMessage);
    }


    @Test
    @DisplayName("тест метода allRoomMessage на состояние NullPointerException (room = null)")
    void testAllRoomMessage_room_null() {
        User user = generateUser();
        Optional<Room> room = Optional.of(generateRoom());

        when(userRepository.findByEmail("test@test")).thenReturn(user);
        when(roomRepository.findByIdAndUsers(room.get().getId(), user)).thenReturn(null);
        when(authentication.getName()).thenReturn("test@test");

        assertThrows(NullPointerException.class,
                () -> messageService.allRoomMessage(room.get().getId(), authentication));
    }


    @Test
    @DisplayName("тест метода inputUserMessage на состояние ок")
    void inputUserMessage_ok() {
        MessageDto messageDto = generateMessageDto();

        Message message = new Message();
        User user = generateUser();

        MessageDto expectedMessageDto = generateMessageDto();

        when(modelMapper.map(messageDto, Message.class)).thenReturn(message);
        when(modelMapper.map(message, MessageDto.class)).thenReturn(messageDto);

        when(authentication.getName()).thenReturn("test@test");
        when(userRepository.findByEmail("test@test")).thenReturn(user);

        MessageDto result = messageService.inputUserMessage(messageDto, authentication);
        assertEquals(expectedMessageDto.getText(), result.getText());
    }


    @Test
    @DisplayName("тест метода inputUserMessage когда сообщение null")
    void inputUserMessage_not_ok() {
        MessageDto messageDto = new MessageDto();
        messageDto.setText(null);

        Message message = new Message();
        User user = generateUser();

        MessageDto expectedMessageDto = new MessageDto();
        expectedMessageDto.setText(null);

        when(modelMapper.map(messageDto, Message.class)).thenReturn(message);
        lenient().when(modelMapper.map(message, MessageDto.class)).thenReturn(messageDto); // lenient(). делает ненужные моки мягкими

        when(authentication.getName()).thenReturn("test@test");
        when(userRepository.findByEmail("test@test")).thenReturn(user);

        MessageDto result = messageService.inputUserMessage(messageDto, authentication);

        assertNull(result, "Expected result to be null when input message text is null");

       // assertEquals(expectedMessageDto.getText(), result.getText());

}

    @Test
    @DisplayName("тест метода inputUserMessage когда сообщение пробел")
    void inputUserMessage_space() {

        MessageDto messageDto = new MessageDto();
        messageDto.setText(" ");

        Message message = new Message();
        User user = generateUser();

        MessageDto expectedMessageDto = new MessageDto();
        expectedMessageDto.setText(null);

        when(modelMapper.map(messageDto, Message.class)).thenReturn(message);
        lenient().when(modelMapper.map(message, MessageDto.class)).thenReturn(messageDto);

        when(authentication.getName()).thenReturn("test@test");
        when(userRepository.findByEmail("test@test")).thenReturn(user);

        MessageDto result = messageService.inputUserMessage(messageDto, authentication);


        assertNull(result, "Expected result to be null when input message text is null");
       // assertEquals(null, result.getText());
    }

    @Test
    @DisplayName("тест метода inputUserMessage когда сообщение пустое")
    void inputUserMessage_pusto() {
        MessageDto messageDto = new MessageDto();
        messageDto.setText("");

        Message message = new Message();
        User user = generateUser();

        MessageDto expectedMessageDto = new MessageDto();
        expectedMessageDto.setText(null);

        when(modelMapper.map(messageDto, Message.class)).thenReturn(message);
        lenient().when(modelMapper.map(message, MessageDto.class)).thenReturn(messageDto);

        when(authentication.getName()).thenReturn("test@test");
        when(userRepository.findByEmail("test@test")).thenReturn(user);

        MessageDto result = messageService.inputUserMessage(messageDto, authentication);

        assertNull(result, "Expected result to be null when input message text is null");
       // assertEquals(null, result.getText());
    }

    private Room generateRoom() {
        return Room.builder()
                .id(UUID.randomUUID())
                .name("Test")
                .info("test1")
                .build();
    }

    private User generateUser() {
        return User.builder()
                .id(1l)
                .name("test")
                .email("test@test")
                .password("test").build();
    }

    private MessageDto generateMessageDto() {
        return MessageDto.builder().text("test").build();

    }

}