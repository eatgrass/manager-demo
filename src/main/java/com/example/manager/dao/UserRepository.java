package com.example.manager.dao;

import com.example.manager.model.User;

public interface UserRepository {

    User findById(long userId);

    void save(User user);

}
