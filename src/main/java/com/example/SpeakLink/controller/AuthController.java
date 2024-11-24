package com.example.SpeakLink.controller;

import com.example.SpeakLink.Mapper.UserMapper;
import com.example.SpeakLink.service.UserService;
import com.example.SpeakLink.dto.UserDto;
import com.example.SpeakLink.entity.User;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AuthController {

    private final UserService userService;


    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/chat")
    public String chat(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.findByEmail(authentication.getName());
        model.addAttribute("authUser", currentUser);

        List<UserDto> users = new ArrayList<>();
        for (UserDto userDto : userService.findAllUsers(currentUser)) {
            if (!userDto.getEmail().equals(currentUser.getEmail())) {
                users.add(userDto);
            }
        }
        model.addAttribute("users", users);

        return "chat";
    }

    @PostMapping("/chat/user")
    @ResponseBody
    public Object getChatUser(Authentication authentication) {
        return new Object() {
            final String email = authentication.getName();

            public String getEmail() {
                return email;
            }
        };
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @GetMapping("register")
    public String showRegistrationForm(Model model) {
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto user,
                               BindingResult result,
                               Model model) {
        User existing = userService.findByEmail(user.getEmail());
        if (existing != null) {
            result.rejectValue("email", null, "There is already an account registered with that email");
        }
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "register";
        }
        userService.saveUser(user);
        return "redirect:/register?success";
    }



//    TODO test
//    @GetMapping("/profile")
//    public String showProfileForm(Model model, Authentication authentication) {
//        // Получаем информацию о пользователе через authentication
//        User user = userService.findByEmail(authentication.getName());
//
//        UserDto userDto = UserMapper.INSTANCE.toUserDTO(user);
//
//        // Добавляем объект userDto в модель, чтобы он был доступен в форме
//        model.addAttribute("editUser", userDto);
//
//        // Возвращаем имя шаблона, который будет отображать страницу профиля
//        return "profile";
//    }
//
//
//
//    @PostMapping("/chat")
//    public String editUser(@ModelAttribute("editUser") UserDto userDto, Authentication authentication) {
//        // Сохраняем изменения пользователя
//        userService.editUser(authentication, userDto);
//
//        // Редирект на страницу профиля после успешного сохранения
//        return "redirect:/profile";
//    }

}
