package com.example.manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.manager.exception.AuthorizationException;
import com.example.manager.model.Permission;
import com.example.manager.model.User;
import com.example.manager.security.SecurityContext;
import com.example.manager.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/{resource}")
    public ResponseEntity<String> getUserResource(@PathVariable String resource) {

        Long userId = SecurityContext.getCurrentUserId();
        User user = userService.findUserById(userId);

        if (user == null) {
            throw new AuthorizationException("user [" + userId + "] doesn't exist");
        }

        if (user.permissions().contains(new Permission(resource))) {
            return ResponseEntity.ok("success");
        } else {
            return ResponseEntity.ok("fail");
        }

    }

}
