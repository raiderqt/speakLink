package com.example.LearningDevProject1.service.impl;

import com.example.LearningDevProject1.dto.RoomDto;
import com.example.LearningDevProject1.entity.Room;
import com.example.LearningDevProject1.entity.User;
import com.example.LearningDevProject1.repository.RoomRepository;
import com.example.LearningDevProject1.repository.UserRepository;
import com.example.LearningDevProject1.service.CorruptedDataException;
import com.example.LearningDevProject1.service.RoomService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class RoomServiceImpl implements RoomService {
	private final RoomRepository roomRepository;
	private final UserRepository userRepository;
	private final ModelMapper modelMapper;

	@Autowired
	public RoomServiceImpl(RoomRepository roomRepository, UserRepository userRepository,
						   ModelMapper modelMapper) {
		this.roomRepository = roomRepository;
		this.userRepository = userRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public List<RoomDto> getRoomList(Authentication authentication) {
		User currentUser = userRepository.findByEmail(authentication.getName());
		return roomRepository.findByUsers(currentUser).stream()
				.map(room -> {
					List<User> allUserByRooms = userRepository.findAllByRooms(room);
					room.setUsers(allUserByRooms);
					RoomDto map = modelMapper.map(room, RoomDto.class);
					if(room.getType().equals(Room.RoomType.PRIVATE))
					{
						try {
							String name = allUserByRooms.stream()
									.filter(user -> !user.equals(currentUser)).findFirst()
									.orElseThrow(CorruptedDataException::new).getName();
							map.setName(name);
						}
						catch (CorruptedDataException e) {
							/* Исключение возникает когда в таблице user_rooms для private комнаты
							 * 	находится меньше 2 связей.
							 */
						}
					}
					return map;
				})
				.toList();
	}
}
