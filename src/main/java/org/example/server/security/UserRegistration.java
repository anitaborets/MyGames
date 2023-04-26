package org.example.server.security;

import jakarta.transaction.Transactional;
import org.example.entity.User;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class UserRegistration {
    private final UserRepository userRepository;
    @Autowired
    public UserRegistration(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Transactional
    public void register(User user){
        userRepository.save(user);
    }
}
