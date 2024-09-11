package com.example.SpeakLink.service.impl;

import com.example.SpeakLink.dto.UserDto;
import com.example.SpeakLink.entity.Role;
import com.example.SpeakLink.entity.User;
import com.example.SpeakLink.repository.RoleRepository;
import com.example.SpeakLink.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
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
    void saveUser() {
        //достаем из базы добавленные конфигом пользователей для test
        List<User> userListTest = userRepository.findAll();

        // создание юзера DTO
        UserDto userDto = new UserDto();
        userDto.setFirstName("Masha");
        userDto.setLastName("Ivanova");
        userDto.setEmail("Ivanova@gmail.com");
        userDto.setPassword("123");

        //user для test
        User userTest = new User();
        userTest.setName(userDto.getFirstName() + " " + userDto.getLastName());
        userTest.setEmail(userDto.getEmail());
        userTest.setPassword(passwordEncoder.encode(userDto.getPassword()));

        //создание роли test
        Role roleTest = roleRepository.findByName("user");
        if(roleTest == null){
            roleTest = checkRoleExist();
            userTest.setRoles(List.of(roleTest));
        }else userTest.setRoles(List.of(roleTest));

        userListTest.add(userTest);

        userService.saveUser(userDto);

        //из базы
        List<User> userList = userRepository.findAll();

        assertEquals(userListTest.get(2).getName(), userList.get(2).getName(), "Имя пользователя не совпадает!");
        assertEquals(userListTest.get(2).getEmail(), userList.get(2).getEmail(), "Email пользователя не совпадает!");

        assertEquals(userListTest.get(2).getRooms(), userList.get(2).getRooms(), "Rooms пользователя не совпадает!");
        assertEquals(userListTest.get(2).getRoles(), userList.get(2).getRoles(), "Roles пользователя не совпадает!");

        assertTrue(passwordEncoder.matches(userDto.getPassword(), userList.get(2).getPassword()), "Пароль не совпадает!");
    }

    private Role checkRoleExist() {
        Role role = new Role();
        role.setName("user");
        return roleRepository.save(role);
    }


    @Test
    @DisplayName("тест метода saveUser когда FirstName null")
    void saveUserNullFirstNameTest() {
        // Создание юзера DTO с null в FirstName
        UserDto userDto = new UserDto();
        userDto.setFirstName(null);
        userDto.setLastName("Ivanova");       //на каждый из создать отдельный тест
        userDto.setEmail("Ivanova@gmail.com");
        userDto.setPassword("123");

        // Ожидаем выброс IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            userService.saveUser(userDto);
        }, "saveUser() выдаст ошибку");
    }

    @Test
    @DisplayName("тест метода saveUser когда FirstName пустое")
    void saveUserEmptiFirstNameTest() {
        // Создание юзера DTO с null в FirstName
        UserDto userDto = new UserDto();
        userDto.setFirstName("");
        userDto.setLastName("Ivanova");       //на каждый из создать отдельный тест
        userDto.setEmail("Ivanova@gmail.com");
        userDto.setPassword("123");

        // Ожидаем выброс IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            userService.saveUser(userDto);
        }, "saveUser() выдаст ошибку");
    }

    @Test
    @DisplayName("тест метода saveUser когда LastName null")
    void saveUserNullLastNameTest() {
        // Создание юзера DTO с null в FirstName
        UserDto userDto = new UserDto();
        userDto.setFirstName("Masha");
        userDto.setLastName(null);       //на каждый из создать отдельный тест

        userDto.setEmail("Ivanova@gmail.com");
        userDto.setPassword("123");

        // Ожидаем выброс IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            userService.saveUser(userDto);
        }, "saveUser() выдаст ошибку");
    }

    @Test
    @DisplayName("тест метода saveUser когда LastName пустое")
    void saveUserEmptiLastNameTest() {
        // Создание юзера DTO с null в FirstName
        UserDto userDto = new UserDto();
        userDto.setFirstName("Masha");
        userDto.setLastName("");       //на каждый из создать отдельный тест
        userDto.setEmail("Ivanova@gmail.com");
        userDto.setPassword("123");

        // Ожидаем выброс IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            userService.saveUser(userDto);
        }, "saveUser() выдаст ошибку");
    }

    @Test
    @DisplayName("тест метода saveUser когда Email null")
    void saveUserNullEmailTest() {
        // Создание юзера DTO с null в FirstName
        UserDto userDto = new UserDto();
        userDto.setFirstName("Masha");
        userDto.setLastName("Ivanova");       //на каждый из создать отдельный тест

        userDto.setEmail(null);
        userDto.setPassword("123");

        // Ожидаем выброс IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            userService.saveUser(userDto);
        }, "saveUser() выдаст ошибку");
    }

    @Test
    @DisplayName("тест метода saveUser когда Email пустое")
    void saveUserEmptiEmailTest() {
        // Создание юзера DTO с null в FirstName
        UserDto userDto = new UserDto();
        userDto.setFirstName("Masha");
        userDto.setLastName("Ivanova");       //на каждый из создать отдельный тест
        userDto.setEmail("");
        userDto.setPassword("123");

        // Ожидаем выброс IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            userService.saveUser(userDto);
        }, "saveUser() выдаст ошибку");
    }

    @Test
    @DisplayName("тест метода saveUser когда Password null")
    void saveUserNullPasswordTest() {
        // Создание юзера DTO с null в FirstName
        UserDto userDto = new UserDto();
        userDto.setFirstName("Masha");
        userDto.setLastName("Ivanova");       //на каждый из создать отдельный тест

        userDto.setEmail("Ivanova@gmail.com");
        userDto.setPassword(null);

        // Ожидаем выброс IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            userService.saveUser(userDto);
        }, "saveUser() выдаст ошибку");
    }

    @Test
    @DisplayName("тест метода saveUser когда Password пустое")
    void saveUserEmptiPasswordTest() {
        // Создание юзера DTO с null в FirstName
        UserDto userDto = new UserDto();
        userDto.setFirstName("Masha");
        userDto.setLastName("Ivanova");       //на каждый из создать отдельный тест
        userDto.setEmail("Ivanova@gmail.com");
        userDto.setPassword("");

        // Ожидаем выброс IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            userService.saveUser(userDto);
        }, "saveUser() выдаст ошибку");
    }
}