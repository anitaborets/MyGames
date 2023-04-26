package org.example.server.webcontroller;

import jakarta.validation.Valid;
import org.example.entity.User;
import org.example.server.security.UserDetails;
import org.example.server.security.UserRegistration;
import org.example.server.security.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.WebApplicationContext;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class UserController {
    @Autowired
    private AuthenticationManager authenticationManager;
    private final UserRegistration userRegistration;
    private final UserValidator userValidator;

    @Autowired
    public UserController(UserRegistration userRegistration, UserValidator userValidator) {
        this.userRegistration = userRegistration;
        this.userValidator = userValidator;
    }

    @GetMapping("/login")
    public String index() {
        return "login";
    }

    @GetMapping("/showUserInfo")
    public String showUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails details = (UserDetails) authentication.getPrincipal();
        System.out.println(details.getUser());
        return "index";
    }

    @PostMapping(value = "/registration")
    public String addUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);
        if(bindingResult.hasErrors()){
            return "index";
        }
        userRegistration.register(user);
        return "index";
    }

}

