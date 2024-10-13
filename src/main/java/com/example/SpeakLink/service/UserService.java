package com.example.SpeakLink.service;

import com.example.SpeakLink.dto.UserDto;
import com.example.SpeakLink.entity.User;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface UserService {
    User saveUser(UserDto userDto);

    User findByEmail(String email);

    List<UserDto> findAllUsers();

    void editUser(Authentication authentication, UserDto userDto);
}
