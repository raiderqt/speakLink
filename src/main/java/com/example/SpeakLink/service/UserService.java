package com.example.SpeakLink.service;

import com.example.SpeakLink.dto.UserDto;
import com.example.SpeakLink.entity.User;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface UserService {

    User saveUser(UserDto userDto);

    User findUserByEmail(String email);

    List<UserDto> findUserByName(String name);

    List<UserDto> findAllUsers(User user);

    List<UserDto> findAllUsers();

    void saveFriend(Long id , Long friendId);

    User findUserById(Long id);

    void editUser(Authentication authentication, UserDto userDto);


}
