package com.example.SpeakLink.controller.chat;

import com.example.SpeakLink.dto.AddUsersToRoomDTO;
import com.example.SpeakLink.dto.RoomDto;
import com.example.SpeakLink.dto.UserDto;

import com.example.SpeakLink.service.RoomService;
import com.example.SpeakLink.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;


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

    @PostMapping("/chat/group")
    public ResponseEntity<String> createGroup(Authentication authentication, @RequestBody String name) {
        Long userId = userService.findUserByEmail(authentication.getName()).getId();
        RoomDto roomDto = null;
        try {
            roomDto = objectMapper.readValue(name, RoomDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        roomService.createGroup(userId, roomDto.getName());
        return ResponseEntity.ok("Группа " + name + "создана");
    }

    @GetMapping("/chat/users")
    public @ResponseBody List<UserDto> findUserToGroup() {
        return userService.findAllUsers();
    }


    @PostMapping("/chat/usersList")
    public ResponseEntity<String> addUserToGroup(@Valid @RequestBody AddUsersToRoomDTO addUsersToRoomDTO, Authentication authentication) {
        Long userId = userService.findUserByEmail(authentication.getName()).getId();
        UUID roomId = addUsersToRoomDTO.getRoomId();   // Получаем UUID комнаты
        List<Long> users = addUsersToRoomDTO.getUsers();
        roomService.addUserToRoom(users, roomId, userId);

        return ResponseEntity.ok("Пользователи успешно добавлены");
    }
}