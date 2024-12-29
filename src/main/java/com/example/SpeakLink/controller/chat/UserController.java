package com.example.SpeakLink.controller.chat;

import com.example.SpeakLink.dto.UserDto;
import com.example.SpeakLink.service.UserService;

import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import java.util.List;

@RestController
public class UserController {

    private final ObjectMapper objectMapper;
    private final UserService  userService;

    public UserController(ObjectMapper objectMapper, UserService userService) {
        this.objectMapper = objectMapper;
        this.userService = userService;
    }


    @PostMapping("/chat/find")
    public @ResponseBody List<UserDto> findFriend(@RequestBody String firstName)  {
        UserDto user = null;
        try {
            user = objectMapper.readValue(firstName , UserDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return userService.findUserByName(user.getFirstName());
    }

    @PostMapping("/profile")
    public ResponseEntity<Map<String, Object>> editUser(
            @RequestBody Map<String, String> requestBody, Authentication authentication) {

        String firstName = requestBody.get("firstName");
        String lastName = requestBody.get("lastName");

        if ((!firstName.isBlank()) && (!lastName.isBlank())) {
            UserDto userDto = new UserDto();
            userDto.setFirstName(firstName);
            userDto.setLastName(lastName);

            userService.editUser(authentication, userDto);
        } else {
            // Если пароли не совпадают, возвращаем ошибку
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "даные не введены"));
        }
        return ResponseEntity.ok(Map.of("success", true));
    }

    @PostMapping("/chat/addFriend")
    public ResponseEntity<String> addFriend(Authentication authentication , @RequestBody String id) {
        Long userId = userService.findUserByEmail(authentication.getName()).getId();
        UserDto userDto = null;
        try {
            userDto = objectMapper.readValue(id , UserDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        userService.saveFriend(userId , userService.findUserById(userDto.getId()).getId());
        return ResponseEntity.ok("Вы добавили друга" + userDto.getFirstName() + " " + userDto.getLastName());
    }

}


