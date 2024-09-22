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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class RoomServiceImpl implements RoomService {
	private final RoomRepository roomRepository;
	private final UserRepository userRepository;
	private final ModelMapper modelMapper;


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
