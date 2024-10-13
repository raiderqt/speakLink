package com.example.SpeakLink.service.impl;

import com.example.SpeakLink.Mapper.UserMapper;
import com.example.SpeakLink.repository.UserRepository;
import com.example.SpeakLink.service.UserService;
import com.example.SpeakLink.dto.UserDto;
import com.example.SpeakLink.entity.Role;
import com.example.SpeakLink.entity.User;
import com.example.SpeakLink.repository.RoleRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService
{

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    /**
     * создает и добавляет нового пользователя
     * @param userDto - user(пользователь)
     */

    @Override
    public User saveUser(UserDto userDto) {
        if(!userDto.getFirstName().isBlank() && !userDto.getLastName().isBlank() && !userDto.getEmail().isBlank()
            && !userDto.getPassword().isBlank()){

            User user = UserMapper.INSTANCE.toUser(userDto);
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            Optional<Role> roleOptional = roleRepository.findByName("user");
            Role role = roleOptional.orElseGet(this::createRoleExist);
            user.setRoles(List.of(role));

            userRepository.save(user);
            log.info("Пользователь зарегистрирован  {}, {} {} ",userDto.getEmail(),userDto.getFirstName(),userDto.getLastName());
            return user;
        } else {
            log.info("Не удалось зарегистрировать пользователя {}, {} {} ",userDto.getEmail(),userDto.getFirstName(),userDto.getLastName());
            return null;
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


    @Override
    public void editUser(Authentication authentication, UserDto userDto) {
        User user = userRepository.findByEmail(authentication.getName());

        user.setName(userDto.getFirstName() + " " + userDto.getLastName());

        userRepository.save(user);
    }



    private UserDto convertEntityToDto(User user){
        UserDto userDto = new UserDto();
        String[] name = user.getName().split(" ");
        userDto.setFirstName(name[0]);
        userDto.setLastName(name[1]);
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    private Role createRoleExist() {
        Role role = new Role();
        role.setName("user");
        return roleRepository.save(role);
    }

}
