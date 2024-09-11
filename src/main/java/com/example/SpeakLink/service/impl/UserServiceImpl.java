package com.example.SpeakLink.service.impl;

import com.example.SpeakLink.repository.UserRepository;
import com.example.SpeakLink.service.UserService;
import com.example.SpeakLink.dto.UserDto;
import com.example.SpeakLink.entity.Role;
import com.example.SpeakLink.entity.User;
import com.example.SpeakLink.repository.RoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService
{

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }



    /**
     * создает и добавляет нового пользователя
     * @param userDto - user(пользователь)
     */

    @Override
    public void saveUser(UserDto userDto) {

        //проверка на null
        if (userDto.getFirstName() != null && !userDto.getFirstName().isEmpty() &&
                userDto.getLastName() != null && !userDto.getLastName().isEmpty() &&
                userDto.getEmail() != null && !userDto.getEmail().isEmpty() &&
                userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {

            User user = new User();
            user.setName(userDto.getFirstName() + " " + userDto.getLastName());
            user.setEmail(userDto.getEmail());

            //encrypt the password once we integrate spring security
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));

            Role role = roleRepository.findByName("user");
            if(role == null){
                role = checkRoleExist();
                user.setRoles(List.of(role));
            }else user.setRoles(List.of(role));

            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("Имя и фамилия не должны быть нулевыми или пустыми");
        }
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    private UserDto convertEntityToDto(User user){
        UserDto userDto = new UserDto();
        String[] name = user.getName().split(" ");
        userDto.setFirstName(name[0]);
        userDto.setLastName(name[1]);
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    private Role checkRoleExist() {
        Role role = new Role();
        role.setName("user");
        return roleRepository.save(role);
    }

}
