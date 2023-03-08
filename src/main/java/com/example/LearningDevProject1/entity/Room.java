package com.example.LearningDevProject1.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "room")
public class Room
{
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	//@Type(type="uuid-char")
	private UUID id;

	@Column(nullable = false)
	private String name;
	private String info;

	public enum RoomType
	{
		PRIVATE,
		PUBLIC,
		CLOSED
	}
	@Column(nullable = false)
	private RoomType type;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			name = "users_rooms",
			joinColumns = {@JoinColumn(name = "ROOM_ID", referencedColumnName = "ID")},
			inverseJoinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")})
	private List<User> users = new ArrayList<>();
}
