package org.copilot.service;

import org.copilot.model.User;
import org.copilot.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<String> getAllUserNames() {
        return userRepository.findAll().stream().map(User::getName).toList();
    }

    public void saveUser(String name) {
        userRepository.save(new User(name));
    }
}
