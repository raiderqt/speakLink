package com.example.SpeakLink.service;

import com.example.SpeakLink.dto.UserDto;
import com.example.SpeakLink.entity.User;

import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto);

    User findByEmail(String email);

    List<UserDto> findAllUsers();
}
