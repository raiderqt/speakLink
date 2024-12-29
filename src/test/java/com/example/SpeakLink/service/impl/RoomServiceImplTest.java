package com.example.SpeakLink.service.impl;

import com.example.SpeakLink.dto.RoomDto;
import com.example.SpeakLink.entity.Room;
import com.example.SpeakLink.entity.User;
import com.example.SpeakLink.repository.RoomRepository;
import com.example.SpeakLink.repository.UserRepository;
import com.example.SpeakLink.service.CorruptedDataException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceImplTest {

    @Mock
    UserRepository userRepository;
    @Mock
    RoomRepository roomRepository;
    @Mock
    ModelMapper modelMapper;
    @InjectMocks
    RoomServiceImpl roomServiceImpl;

    @Test
    @DisplayName("тест метода getRoomList на состояние ок")
    void testGetRoomList_Ok() {
        User user = generateUser();

    }

    private User generateUser() {
        return User.builder()
                .id(1l)
                .name("test")
                .email("test@test")
                .password("test").build();
    }
}
//public List<RoomDto> getRoomList(Authentication authentication) {
//    User currentUser = userRepository.findByEmail(authentication.getName());
//    return roomRepository.findByUsers(currentUser).stream()
//            .map(room -> {
//                List<User> allUserByRooms = userRepository.findAllByRooms(room);
//                room.setUsers(allUserByRooms);
//                RoomDto map = modelMapper.map(room, RoomDto.class);
//                if(room.getType().equals(Room.RoomType.PRIVATE))
//                {
//                    try {
//                        String name = allUserByRooms.stream()
//                                .filter(user -> !user.equals(currentUser)).findFirst()
//                                .orElseThrow(CorruptedDataException::new).getName();
//                        map.setName(name);
//                    }
//                    catch (CorruptedDataException e) {
//                        /* Исключение возникает когда в таблице user_rooms для private комнаты
//                         * 	находится меньше 2 связей.
//                         */
//                    }
//                }
//                return map;
//            })
//            .toList();
//}