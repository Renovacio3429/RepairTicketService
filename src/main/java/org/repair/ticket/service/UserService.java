package org.repair.ticket.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.repair.ticket.model.User;
import org.repair.ticket.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }
}
