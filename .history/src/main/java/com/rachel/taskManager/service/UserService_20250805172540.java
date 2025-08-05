package com.rachel.taskManager.service;

import com.rachel.taskManager.model.User;
import com.rachel.taskManager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User saveOrGetUser(String sub, String email) {
        return userRepository.findById(sub).orElseGet(() -> {
            User newUser = new User(sub, email);
            return userRepository.save(newUser);
        });
    }

    public Optional<User> getUserBySub(String sub) {
        return userRepository.findById(sub);
    }
}
