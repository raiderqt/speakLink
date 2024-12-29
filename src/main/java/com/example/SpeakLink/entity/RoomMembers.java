package com.example.SpeakLink.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "users_rooms", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "room_id"})})
public class RoomMembers {
    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    @Embeddable
    public static class RoomMembersPk implements Serializable {
        @ManyToOne
        @JoinColumn(name = "user_id")
        private User userId;

        @ManyToOne
        @JoinColumn(name = "room_id")
        private Room roomId;
    }

    @EmbeddedId
    private RoomMembersPk id;

    private String role;

    @ManyToOne
    @JoinColumn()
    private User inviter;

    @Column(name = "invite_status", nullable = false, columnDefinition = "boolean default false")
    private boolean inviteStatus;
}
