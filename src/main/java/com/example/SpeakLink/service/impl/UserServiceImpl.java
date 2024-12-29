package com.example.SpeakLink.service.impl;

import com.example.SpeakLink.Mapper.UserMapper;
import com.example.SpeakLink.entity.Room;
import com.example.SpeakLink.entity.RoomMembers;
import com.example.SpeakLink.repository.RoomMembersRepository;
import com.example.SpeakLink.repository.RoomRepository;
import com.example.SpeakLink.repository.UserRepository;
import com.example.SpeakLink.service.RoomService;
import com.example.SpeakLink.service.UserService;
import com.example.SpeakLink.dto.UserDto;
import com.example.SpeakLink.entity.Role;
import com.example.SpeakLink.entity.User;
import com.example.SpeakLink.repository.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoomRepository roomRepository;
    private final RoomService roomService;
    private final UserMapper userMapper;
    private final RoomMembersRepository roomMembersRepository;

    /**
     * создает и добавляет нового пользователя
     *
     * @param userDto - user(пользователь)
     */

    @Override
    public User saveUser(UserDto userDto) {
        if (!userDto.getFirstName().isBlank() && !userDto.getLastName().isBlank() && !userDto.getEmail().isBlank()
                && !userDto.getPassword().isBlank()) {

            User user = UserMapper.INSTANCE.toUser(userDto);
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            Optional<Role> roleOptional = roleRepository.findByName("user");
            Role role = roleOptional.orElseGet(this::createRoleExist);
            user.setRoles(List.of(role));

            userRepository.save(user);
            log.info("Пользователь зарегистрирован  {}, {} {} ", userDto.getEmail(), userDto.getFirstName(), userDto.getLastName());
            return user;
        } else {
            log.info("Не удалось зарегистрировать пользователя {}, {} {} ", userDto.getEmail(), userDto.getFirstName(), userDto.getLastName());
            return null;
        }
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserDto> findAllUsers(User currentUser) {
        List<User> users = new ArrayList<>(currentUser.getFriends());
        return users.stream().map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void editUser(Authentication authentication, UserDto userDto) {
        User user = userRepository.findByEmail(authentication.getName());
        user.setName(userDto.getFirstName() + " " + userDto.getLastName());
        userRepository.save(user);
    }

    private UserDto convertEntityToDto(User user) {
        UserDto userDto = new UserDto();
        String[] name = user.getName().split(" ");
        userDto.setFirstName(name[0]);
        userDto.setLastName(name[1]);
        userDto.setEmail(user.getEmail());
        userDto.setId(user.getId());
        return userDto;
    }

    private Role createRoleExist() {
        Role role = new Role();
        role.setName("user");
        return roleRepository.save(role);
    }

    @Override
    public List<UserDto> findUserByName(String name) {
        List<UserDto> userDto = new ArrayList<>();
        for (User user : userRepository.findAllByNameStartingWith(name)) {
            userDto.add(userMapper.toUserDTO(user));
        }
        return userDto;
    }

    @Override
    public void saveFriend(Long userId, Long friendId) {
        User user = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
        User friend = userRepository.findById(friendId).orElseThrow(EntityNotFoundException::new);
        Room room = null;
        String nameRoom = user.getName() + " " + "and" + " " + friend.getName();
        boolean add = user.getFriends().add(friend);
        if (add) {
            room = roomService.createPrivateRoom(friend, nameRoom);
            room.getUsers().add(user);
            room.getUsers().add(friend);
            roomRepository.save(room);
            userRepository.save(user);
            roomMembersRepository.save(new RoomMembers(new RoomMembers.RoomMembersPk(user, room), room.getInfo(), friend, true));
            roomMembersRepository.save(new RoomMembers(new RoomMembers.RoomMembersPk(friend, room), room.getInfo(), user, true));
        }
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }


}

