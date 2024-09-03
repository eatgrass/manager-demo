package com.example.manager.service;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.manager.dao.UserRepository;
import com.example.manager.model.User;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    public void addUser(User user) {

        Objects.nonNull(user);
        userRepository.save(user);

    }

}
