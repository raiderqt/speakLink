package com.example.SpeakLink.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddUsersToRoomDTO {
    @NotNull(message = "Room ID cannot be null")
    private UUID roomId;

    @NotEmpty(message = "Users list cannot be empty")
    private List<Long> users;

}
