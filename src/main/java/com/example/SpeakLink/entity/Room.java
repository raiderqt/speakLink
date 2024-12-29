package com.example.SpeakLink.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "room")
public class Room {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(nullable = false)
    private String name;
    private String info;

    public enum RoomType {
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

    private Set<User> users = new HashSet<>();

    public Room(String name, String info, RoomType type, Set<User> users) {
        this.name = name;
        this.info = info;
        this.type = type;
        this.users = users;
    }
}
