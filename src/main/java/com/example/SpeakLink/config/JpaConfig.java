package com.example.SpeakLink.config;

import com.example.SpeakLink.entity.*;
import com.example.SpeakLink.repository.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
@Configuration
@EnableTransactionManagement
public class JpaConfig {
	private final RoleRepository roleRepository;
	private final RoomRepository roomRepository;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final MessageRepository messageRepository;
	private final RoomMembersRepository roomMembersRepository;

	public JpaConfig(RoleRepository roleRepository, RoomRepository roomRepository, UserRepository userRepository,
					 PasswordEncoder passwordEncoder, RoomMembersRepository roomMembersRepository,
					 MessageRepository messageRepository) {
		this.roleRepository = roleRepository;
		this.roomRepository = roomRepository;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.roomMembersRepository = roomMembersRepository;
		this.messageRepository = messageRepository;
	}

	@PostConstruct
	public void initDataForTest() {
		String roleName = "user";
		Role roleUser = roleRepository.findByName(roleName);
		if (roleUser == null) {
			roleUser = new Role();
			roleUser.setName("user");
			roleRepository.saveAndFlush(roleUser);
		}

		String testRoomName = "Test Room";
		Room roomTest = roomRepository.findByName(testRoomName);
		if (roomTest == null) {
			roomTest = new Room();
			roomTest.setName(testRoomName);
			roomTest.setInfo("Тестовая комната");
			roomTest.setType(Room.RoomType.PUBLIC);
			roomRepository.save(roomTest);
		}

		String userMailName = "test@test";
		User testUser = createUserAndJoinRoom(userMailName, "Test User", roomTest, roleUser);
		userMailName = "Admin@test";
		User testAdmin = createUserAndJoinRoom(userMailName, "Test Admin", roomTest, roleUser);

		testRoomName = "Admin and test user private room for test";
		Room privateRoom = roomRepository.findByName(testRoomName);
		if (privateRoom == null) {
			privateRoom = new Room();
			privateRoom.setName(testRoomName);
			privateRoom.setInfo("Тестовая личка");
			privateRoom.setType(Room.RoomType.PRIVATE);
			privateRoom.getUsers().addAll(List.of(testUser, testAdmin));
			roomRepository.save(privateRoom);
		}

		//Инвайт статус личек в true
		Optional<RoomMembers> roomMember = roomMembersRepository.findById(new RoomMembers.RoomMembersPk(testUser, privateRoom));
		roomMember.ifPresent(current -> {
			current.setInviteStatus(true);
			roomMembersRepository.save(current);
		});
		roomMember = roomMembersRepository.findById(new RoomMembers.RoomMembersPk(testAdmin, privateRoom));
		roomMember.ifPresent(current -> {
			current.setInviteStatus(true);
			roomMembersRepository.save(current);
		});

		//Сообщения для теста
		Instant instant = Instant.now();
		Message message = new Message();
		message.setRoom(privateRoom);
		message.setUser(testAdmin);
		message.setTimestamp(
				Timestamp.from(instant.minus(120, ChronoUnit.MINUTES)));
		message.setText("Проверка 1");
		messageRepository.save(message);

		message.setId(null);
		message.setUser(testUser);
		message.setText("Проверка 2");
		message.setTimestamp(
				Timestamp.from(instant.minus(110, ChronoUnit.MINUTES)));
		messageRepository.save(message);

		message.setId(null);
		message.setUser(testAdmin);
		message.setText("Проверка 3");
		message.setTimestamp(
				Timestamp.from(instant.minus(100, ChronoUnit.MINUTES)));
		messageRepository.save(message);

		message.setId(null);
		message.setUser(testUser);
		message.setText("Проверка 4");
		message.setTimestamp(
				Timestamp.from(instant.minus(89, ChronoUnit.MINUTES)));
		messageRepository.save(message);

		message.setId(null);
		message.setUser(testAdmin);
		message.setText("Проверка 5");
		message.setTimestamp(
				Timestamp.from(instant.minus(75, ChronoUnit.MINUTES)));
		messageRepository.save(message);

		message.setId(null);
		message.setUser(testUser);
		message.setText("Проверка 6");
		message.setTimestamp(
				Timestamp.from(instant.minus(71, ChronoUnit.MINUTES)));
		messageRepository.save(message);

		message.setId(null);
		message.setUser(testAdmin);
		message.setText("Старт");
		message.setTimestamp(
				Timestamp.from(instant.minus(125, ChronoUnit.MINUTES)));
		messageRepository.save(message);
	}

	private User createUserAndJoinRoom(String email, String name, Room room, Role roleUser) {
		User user = userRepository.findByEmail(email);
		if (user == null) {
			user = new User();
			user.setEmail(email);
			user.setPassword(passwordEncoder.encode("test"));
			user.setName(name);
			user.setRoles(List.of(roleUser));
			user.setRooms(List.of(room));
			userRepository.save(user);
			Optional<RoomMembers> roomMap = roomMembersRepository
					.findById(new RoomMembers.RoomMembersPk(user, room));
			roomMap.orElseThrow().setInviteStatus(true);
			roomMembersRepository.save(roomMap.get());
		}
		return user;
	}
}
