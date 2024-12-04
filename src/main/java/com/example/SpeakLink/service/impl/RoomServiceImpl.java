package com.example.SpeakLink.service.impl;

import com.example.SpeakLink.dto.RoomDto;
import com.example.SpeakLink.entity.Room;
import com.example.SpeakLink.entity.User;
import com.example.SpeakLink.repository.RoomRepository;
import com.example.SpeakLink.repository.UserRepository;
import com.example.SpeakLink.service.CorruptedDataException;
import com.example.SpeakLink.service.RoomService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public Room createPrivateRoom(User user,  String nameRoom) {
        return new Room(user.getName(),nameRoom, Room.RoomType.PRIVATE, new HashSet<>());
    }

    @Override
    public Room createPublicRoom(User user,  String nameRoom) {
        return new Room(user.getName(),nameRoom, Room.RoomType.PUBLIC, new HashSet<>());
    }

    @Override
    public Set<RoomDto> getRoomList(Authentication authentication) {
        User currentUser = userRepository.findByEmail(authentication.getName());
        /* Исключение возникает когда в таблице user_rooms для private комнаты
         * 	находится меньше 2 связей.
         */
        Set<RoomDto> list = new HashSet<>();
        for (Room room1 : roomRepository.findByUsers(currentUser)) {
            Set<User> allUserByRooms = userRepository.findAllByRooms(room1);
            room1.setUsers(allUserByRooms);
            RoomDto map = modelMapper.map(room1, RoomDto.class);
            if (room1.getType().equals(Room.RoomType.PRIVATE)) {
                try {
                    String name = allUserByRooms.stream()
                            .filter(user -> !user.equals(currentUser)).findFirst()
                            .orElseThrow(CorruptedDataException::new).getName();
                    map.setName(name);
                } catch (CorruptedDataException e) {
                    /* Исключение возникает когда в таблице user_rooms для private комнаты
                     * 	находится меньше 2 связей.
                     */
                }
            }
            RoomDto apply = map;
            list.add(apply);
        }
        return list;
    }
}
