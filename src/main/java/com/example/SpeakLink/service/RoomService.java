package com.example.SpeakLink.service;

import com.example.SpeakLink.dto.RoomDto;
import com.example.SpeakLink.entity.Room;
import com.example.SpeakLink.entity.User;
import org.springframework.security.core.Authentication;

import java.util.List;
/**
 *	Концепция: room представляют собой связь между пользователями и разделяются на типы.
 *	Типы:
 *	private: связь между двумя пользователями (просто личка) должна создавать по следующей логике:
 *		когда пользователь 1 отправляет приглашение на добавление пользователю 2 то
 *		создаётся {@link com.example.SpeakLink.entity.Room} с типом private,
 *		в {@link com.example.SpeakLink.entity.RoomMembers} добавляется запись создателя приглашения и
 *		запись приглашённого в состоянии ожидания
 *		{@link com.example.SpeakLink.entity.RoomMembers#setInviteStatus(boolean)} = false,
 *	public: комната где которая может содержать больше 2 человек, должна создаваться путём соответствующего api,
 *	closed: всё тоже что у публично в добавок доступ к комнате только через приглашения либо по паролю
 *		если такая настройка установлена.
 *
 */

public interface RoomService{

	List<RoomDto> getRoomList(Authentication authentication);

	Room createPrivateRoom(User user , String nameRoom);

}
