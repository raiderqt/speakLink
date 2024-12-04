package com.example.SpeakLink.service.impl;

import com.example.SpeakLink.Mapper.UserMapper;
import com.example.SpeakLink.dto.MessageDto;
import com.example.SpeakLink.dto.RoomDto;
import com.example.SpeakLink.dto.UserDto;
import com.example.SpeakLink.entity.Role;
import com.example.SpeakLink.entity.Room;
import com.example.SpeakLink.entity.RoomMembers;
import com.example.SpeakLink.entity.User;
import com.example.SpeakLink.repository.RoleRepository;
import com.example.SpeakLink.repository.RoomMembersRepository;
import com.example.SpeakLink.repository.RoomRepository;
import com.example.SpeakLink.repository.UserRepository;
import com.example.SpeakLink.service.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static com.example.SpeakLink.entity.Room.RoomType.PRIVATE;
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
    private UserRepository userRepository;   // Мокируем UserRepository

    @Mock
    private RoomRepository roomRepository;   // Мокируем RoomRepository

    @Mock
    private Authentication authentication;   // Мокируем Authentication

    @Mock
    private RoomMembersRepository roomMembersRepository;

    @Mock
    private RoomService roomService;

    @InjectMocks
    private UserServiceImpl userService;     // InjectMocks позволяет Mockito внедрить моки в UserService

    @Mock
    private RoleRepository roleRepository;  // Мокируем RoleRepository

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


    @Test
    @DisplayName("тест метода saveFriend на состояние ок")
    void saveFriend_OK() {

            User user = generateUser();
            User friend = generateFriend();
            String nameRoom = user.getName() + " " + "and" + " " + friend.getName();
            Long userId = user.getId();
            Long friendId = friend.getId();

            Room room = new Room();
            room.setId(UUID.randomUUID());
            room.setName(nameRoom);
            room.setType(PRIVATE);
            room.setUsers(new HashSet<>());
            room.getUsers().add(user);
            room.getUsers().add(friend);

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(userRepository.findById(friendId)).thenReturn(Optional.of(friend));
            when(roomService.createPrivateRoom(friend, nameRoom)).thenReturn(room);

            userService.saveFriend(userId, friendId);


            assertTrue(user.getFriends().contains(friend));
            assertTrue(room.getUsers().contains(user));
            assertTrue(room.getUsers().contains(friend));

            // Verify interactions
            verify(userRepository, times(1)).findById(userId);
            verify(userRepository, times(1)).findById(friendId);
            verify(roomService, times(1)).createPrivateRoom(friend, nameRoom);
            verify(roomRepository, times(1)).save(room);
            verify(userRepository, times(1)).save(user);
            verify(roomMembersRepository, times(2)).save(any(RoomMembers.class));
    }

    @Test
    @DisplayName("тест метода saveFriend на состояние NotOK")
    void saveFriend_NotOK() {
        User user = generateUser();
        Long userId = user.getId();
        Long friendId = 3l;

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findById(friendId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                userService.saveFriend(userId, friendId);
            }
        });

        assertEquals("No value present", exception.getMessage());

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).findById(friendId);
        verifyNoInteractions(roomService, roomRepository, roomMembersRepository);

    }


    private User generateUser() {
        return User.builder()
                .id(1l)
                .name("test")
                .email("test@test")
                .password("test")
                .friends(new HashSet<>()).build();
    }

    private User generateFriend() {
        return User.builder()
                .id(2l)
                .name("test2")
                .email("test2@test2")
                .password("test2")
                .friends(new HashSet<>()).build();
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

// void saveFriend_OK() {
//     // Генерация DTO
//
//     User user = generateUser();
//     User friend = generateFriend();
//
//     when(userRepository.findById(user.getId()));
//     when(userRepository.findById(friend.getId()));
//
//     Room room = null;
//     String nameRoom = user.getName() + " " + "and" + " " + friend.getName();
//     boolean add = user.getFriends().add(friend);
//     if (add) {
//
//         room = roomService.createPrivateRoom(friend, nameRoom);
//         room.getUsers().add(user);
//         room.getUsers().add(friend);
//         roomRepository.save(room);
//         userRepository.save(user);
//
//         roomMembersRepository.save(new RoomMembers(
//                 new RoomMembers.RoomMembersPk(user, room), room.getInfo(), friend, true));
//         roomMembersRepository.save(new RoomMembers(
//                 new RoomMembers.RoomMembersPk(friend, room), room.getInfo(), user, true));
//
//     }
// }
//Long userId = 1L;
//Long friendId = 2L;
//User user = generateUser();
//User friend = generateUser();
//
//when(userRepository.findById(userId)).thenReturn(Optional.of(user));
//when(userRepository.findById(friendId)).thenReturn(Optional.of(friend));
//
//User expectedUser = userRepository.findById(userId).get();
//User expectedFriend = userRepository.findById(friendId).get();
//
//boolean expectedAdd = expectedUser.getFriends().add(friend);
//boolean expectedNotAdd = expectedUser.getFriends().add(friend);
//
//
//
//assertNotNull(expectedUser);
//assertNotNull(expectedFriend);
//assertTrue(expectedAdd);
//assertFalse(expectedNotAdd);
//assertTrue(expectedUser.getFriends().contains(expectedFriend));
//assertEquals(expectedUser.getId(), user.getId());
//assertEquals(expectedFriend.getId(), friend.getId());
//verify(userRepository, times(1)).findById(userId);
//verify(userRepository, times(1)).findById(friendId);
