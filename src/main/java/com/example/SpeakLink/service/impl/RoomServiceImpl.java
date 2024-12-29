package com.example.SpeakLink.service.impl;

import com.example.SpeakLink.dto.RoomDto;
import com.example.SpeakLink.entity.Room;
import com.example.SpeakLink.entity.RoomMembers;
import com.example.SpeakLink.entity.User;
import com.example.SpeakLink.repository.RoomMembersRepository;
import com.example.SpeakLink.repository.RoomRepository;
import com.example.SpeakLink.repository.UserRepository;
import com.example.SpeakLink.service.CorruptedDataException;
import com.example.SpeakLink.service.RoomService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final RoomMembersRepository roomMembersRepository;

    @Override
    public Room createPrivateRoom(User user, String nameRoom) {
        return new Room(user.getName(), nameRoom, Room.RoomType.PRIVATE, new HashSet<>());
    }

    @Override
    public Room createPublicRoom(User user, String nameRoom) {
        return new Room(user.getName(), nameRoom, Room.RoomType.PUBLIC, new HashSet<>());
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
            list.add(map);
        }
        return list;
    }

    @Override
    public void createGroup(Long userId, String groupName) {
        User user = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
        Room room = createPublicRoom(user, groupName);
        room.getUsers().add(user);
        roomRepository.save(room);
        roomMembersRepository.save(new RoomMembers(new RoomMembers.RoomMembersPk(user, room), groupName, user, true));
    }

    @Override
    public void addUserToRoom(List<Long> users, UUID roomId, Long mainUser) {
        Room room = roomRepository.findRoomsById(roomId).orElseThrow(EntityNotFoundException::new);
        User mainUserRoom = userRepository.findById(mainUser).orElseThrow(EntityNotFoundException::new);
        for (Long userId : users) {
            User user = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
            room.getUsers().add(user);
            roomMembersRepository.save(
                    new RoomMembers(new RoomMembers.RoomMembersPk(user, room), room.getInfo(), mainUserRoom, true));
        }

    }
}
