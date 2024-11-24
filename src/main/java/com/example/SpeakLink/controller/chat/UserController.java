package com.example.SpeakLink.controller.chat;

import com.example.SpeakLink.dto.UserDto;
import com.example.SpeakLink.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
        return userService.findByName(user.getFirstName());
    }

    @PostMapping("/chat/addFriend")
    public String addFriend(Authentication authentication , @RequestBody String id) {
       Long userId = userService.findByEmail(authentication.getName()).getId();
       UserDto userDto = null;
        try {
            userDto = objectMapper.readValue(id , UserDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
         userService.saveFriend(userId , userService.findById(userDto.getId()).getId());
        return "redirect:/chat/";
    }
}


