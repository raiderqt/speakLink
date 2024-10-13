package com.example.SpeakLink.controller.chat;

import com.example.SpeakLink.dto.UserDto;
import com.example.SpeakLink.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
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
}





