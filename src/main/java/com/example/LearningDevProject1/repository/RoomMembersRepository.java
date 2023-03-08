package com.example.LearningDevProject1.repository;

import com.example.LearningDevProject1.entity.Room;
import com.example.LearningDevProject1.entity.RoomMembers;
import com.example.LearningDevProject1.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface RoomMembersRepository extends JpaRepository<RoomMembers, RoomMembers.RoomMembersPk>
{
	List<RoomMembers> findAllById_UserId(User user);

	List<RoomMembers> findAllById_RoomId(Room user);
}
