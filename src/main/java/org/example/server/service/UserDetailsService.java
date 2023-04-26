package org.example.server.service;

import org.example.entity.User;
import org.example.repository.UserRepository;
import org.example.server.security.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            Optional<User> user = userRepository.findByUserName(username);
            if(user.isEmpty()){
                throw new UsernameNotFoundException("user not found");
            }
            return new UserDetails(user.get());
    }
}
