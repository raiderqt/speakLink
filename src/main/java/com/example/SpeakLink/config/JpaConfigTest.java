package com.example.SpeakLink.config;

import com.example.SpeakLink.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@AllArgsConstructor
public class JpaConfigTest {
    private final RoleRepository roleRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MessageRepository messageRepository;
    private final RoomMembersRepository roomMembersRepository;

}
