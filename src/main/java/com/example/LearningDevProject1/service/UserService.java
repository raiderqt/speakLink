package com.example.LearningDevProject1.service;

import com.example.LearningDevProject1.dto.UserDto;
import com.example.LearningDevProject1.entity.User;

import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto);

    User findByEmail(String email);

    List<UserDto> findAllUsers();
}
