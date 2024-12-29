package com.example.SpeakLink.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public class AddUsersToRoomDTO {
    @NotNull(message = "Room ID cannot be null")
    private UUID roomId;

    @NotEmpty(message = "Users list cannot be empty")
    private List<Long> users;

    public UUID getRoomId() {
        return roomId;
    }

    public void setRoomId(UUID roomId) {
        this.roomId = roomId;
    }

    public List<Long> getUsers() {
        return users;
    }

    public void setUsers(List<Long> users) {
        this.users = users;
    }
}
