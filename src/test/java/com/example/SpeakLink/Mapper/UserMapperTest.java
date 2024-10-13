package com.example.SpeakLink.Mapper;

import com.example.SpeakLink.dto.UserDto;
import com.example.SpeakLink.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserMapperTest {

    @Test
    @DisplayName("из UserDto в User")
    void toUser() {
        UserDto userDto = generateUserDto();

        User userСurrent =  UserMapper.INSTANCE.toUser(userDto);
        userСurrent.setId(1L);

        User userExpectation = generateUser();

        assertEquals(userExpectation, userСurrent);
    }

    @Test
    @DisplayName("из User в UserDto")
    void toUserDTO(){
        User user = generateUser();

        UserDto userDtoCurrent = UserMapper.INSTANCE.toUserDTO(user);

        UserDto userDtoExpectation = generateUserDto();
        userDtoExpectation.setId(1L);

        assertEquals(userDtoExpectation, userDtoCurrent);
    }


    private UserDto generateUserDto() {
        return UserDto.builder()
                .firstName("Masha")
                .lastName("Ivanova")
                .email("Ivanova@gmail.com")
                .password("123")
                .build();
    }

    private User generateUser() {
        return User.builder()
                .id(1L)
                .name("Masha Ivanova")
                .email("Ivanova@gmail.com")
                .password("123")
                .build();
    }
}