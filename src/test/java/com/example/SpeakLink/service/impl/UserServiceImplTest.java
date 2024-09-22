package com.example.SpeakLink.service.impl;

import com.example.SpeakLink.Mapper.UserMapper;
import com.example.SpeakLink.config.JpaConfigTest;
import com.example.SpeakLink.dto.RoomDto;
import com.example.SpeakLink.dto.UserDto;
import com.example.SpeakLink.entity.Role;
import com.example.SpeakLink.entity.User;
import com.example.SpeakLink.repository.RoleRepository;
import com.example.SpeakLink.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Import(JpaConfigTest.class)  // Импортируем тестовую конфигурацию

class UserServiceImplTest {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    private UserServiceImpl userService;

    @Test
    @DisplayName("тест метода saveUser на состояние ок")
    @Transactional
    void saveUser() {
        List<User> userListTest = new ArrayList<>();
        UserDto userDto = generateUserDto();
        User userTest = UserMapper.INSTANCE.toUser(userDto);
        userTest.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userTest.setId(1L);
        Role role1 = new Role();
        role1.setName("user");
        userTest.setRoles(List.of(role1));
        userListTest.add(userTest);

        userService.saveUser(userDto);
        //из базы
        List<User> userList = userRepository.findAll();

        assertEquals(userListTest.get(0), userList.get(0));
        assertTrue(passwordEncoder.matches(userDto.getPassword(), userList.get(0).getPassword()), "Пароль не совпадает!");
    }

    private UserDto generateUserDto() {
        return UserDto.builder()
                .firstName("Masha")
                .lastName("Ivanova")
                .email("Ivanova@gmail.com")
                .password("123")
                .roomsDTO(generateRoomDtoList())
                .build();
    }

    private List<RoomDto> generateRoomDtoList() {
        List<RoomDto> rooms = new ArrayList<>();
        rooms.add(generateRoomDto());
        return rooms;
    }

    private RoomDto generateRoomDto() {
        return RoomDto.builder()
                .id(UUID.randomUUID())
                .name("Test")
                .info("test1")
                .type("test2")
                .build();
    }
}