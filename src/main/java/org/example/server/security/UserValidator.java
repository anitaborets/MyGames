package org.example.server.security;

import org.example.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
@Component
public class UserValidator implements Validator {

    @Autowired
    public UserValidator(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
    @Autowired
    UserDetailsService userDetailsService;
    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    //tut sa opierame na UserDetailsService - ak dostaneme exceptions, user not exist
    //toto je bed practice a je lepsie mat ešte jeden Userservice ktorý by vracal Optional a ak je empty - vytvotime new User
    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        try {
            userDetailsService.loadUserByUsername(user.getUserName());
        }
        catch (UsernameNotFoundException ignored) {
            return;
            //all is ok
        }
        errors.rejectValue("username","user is not exists");


    }
}
