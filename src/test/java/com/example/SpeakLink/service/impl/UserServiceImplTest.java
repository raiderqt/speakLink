package com.example.SpeakLink.service.impl;

import com.example.SpeakLink.Mapper.UserMapper;
import com.example.SpeakLink.dto.RoomDto;
import com.example.SpeakLink.dto.UserDto;
import com.example.SpeakLink.entity.Role;
import com.example.SpeakLink.entity.User;
import com.example.SpeakLink.repository.RoleRepository;
import com.example.SpeakLink.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;


//@SpringBootTest
//@ActiveProfiles("test")
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
//@Import(JpaConfigTest.class)  // Импортируем тестовую конфигурацию
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private PasswordEncoder passwordEncoder;  // Мок для PasswordEncoder

    @Mock
    private UserRepository userRepository;  // Мокируем UserRepository

    @Mock
    private Authentication authentication;  // Мокируем Authentication

    @InjectMocks
    private UserServiceImpl userService;  // InjectMocks позволяет Mockito внедрить моки в UserService

    @Mock
    private RoleRepository roleRepository;

    @BeforeEach
    public void setUp() {
    }

    //Todo test editUser
    @Test
    @DisplayName("тест метода editUser на состояние ок ")
    void editUser() {
        User mockUser = new User();
        mockUser.setEmail("test@test");
        mockUser.setName("Test User");

        UserDto userDto = new UserDto();
        userDto.setFirstName("NewFirstTest");
        userDto.setLastName("NewLastTest");

        when(authentication.getName()).thenReturn("test@test");
        when(userRepository.findByEmail("test@test")).thenReturn(mockUser);

        User userOld = userRepository.findByEmail("test@test");
        System.out.println("userOld - " + userOld.getName());

        userService.editUser(authentication, userDto);

        assertEquals("NewFirstTest NewLastTest", mockUser.getName());

        User userNew = userRepository.findByEmail("test@test");
        System.out.println("userNew - " + userNew.getName());
    }


    @Test
    @DisplayName("тест метода saveUser на состояние ок")
    void saveUser() {
        // Генерация DTO
        UserDto userDto = generateUserDto();
        User user = UserMapper.INSTANCE.toUser(userDto);

        Role role = new Role();
        role.setName("user");
        user.setRoles(List.of(role));
        user.setId(1L);
        user.setPassword("6776");

        when(roleRepository.findByName("user")).thenReturn(Optional.of(role));
        when(passwordEncoder.encode(userDto.getPassword())).thenReturn("6776");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userService.saveUser(userDto);
        savedUser.setId(1L);

        assertEquals(user, savedUser);
        assertEquals(user.getPassword(), savedUser.getPassword());
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