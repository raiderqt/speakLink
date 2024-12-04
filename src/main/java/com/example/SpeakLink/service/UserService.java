package com.example.SpeakLink.service;

import com.example.SpeakLink.dto.UserDto;
import com.example.SpeakLink.entity.User;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface UserService {

    User saveUser(UserDto userDto);

    User findByEmail(String email);

    List<UserDto> findByName(String name);

    List<UserDto> findAllUsers(User user);

    void saveFriend(Long id , Long friendId);

    User findById (Long id);

    void createGroup(Long userId , String groupName);

    void editUser(Authentication authentication, UserDto userDto);
}
