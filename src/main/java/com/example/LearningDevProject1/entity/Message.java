package com.example.LearningDevProject1.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
@Setter
@Getter
@Entity
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "message", indexes = @Index(columnList = "room_id"))
//@Table(name = "message")
public class Message
{

	public Message(Room room, User user, String text)
	{
		this.room = room;
		this.user = user;
		this.text = text;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	@JoinColumn(name = "room_id", nullable = false)
	private Room room;
	@ManyToOne
	@JoinColumn(nullable = false)
	private User user;
	@Column(nullable = false)
	private Timestamp timestamp;


	@Column(nullable = false)
	private String text;

}
