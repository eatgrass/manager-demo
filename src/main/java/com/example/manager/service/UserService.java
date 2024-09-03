package com.example.manager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.manager.dao.UserRepository;
import com.example.manager.model.User;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findUserById(long userId) {
        return userRepository.findById(userId);
    }
    
}
