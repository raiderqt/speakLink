package com.example.SpeakLink.repository;

import com.example.SpeakLink.entity.Room;
import com.example.SpeakLink.entity.RoomMembers;
import com.example.SpeakLink.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface RoomMembersRepository extends JpaRepository<RoomMembers, RoomMembers.RoomMembersPk>
{
	List<RoomMembers> findAllById_UserId(User user);

	List<RoomMembers> findAllById_RoomId(Room user);
}
