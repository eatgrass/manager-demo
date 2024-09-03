package com.example.manager.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.manager.dto.AddUserRequest;
import com.example.manager.exception.ParameterException;
import com.example.manager.model.Permission;
import com.example.manager.model.Role;
import com.example.manager.model.User;
import com.example.manager.service.AdminService;
import com.example.manager.utils.CollectionUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.Arrays;

@RestController
@RequestMapping("/admin")
public class AdminController {

       @Autowired
       private AdminService adminService;

       @PostMapping("/addUser")
       public ResponseEntity<User> addUser(@RequestBody AddUserRequest payload) {

              if (payload.userId() == null) {
                     throw new ParameterException("userId required");
              }

              if (CollectionUtils.isEmpty(payload.endpoint())) {
                     throw new ParameterException("endpoint required");
              }

              Set<Permission> permissions = Arrays.stream(payload.endpoint()).map(Permission::parse)
                            .collect(Collectors.toSet());
              User user = new User(payload.userId(), Role.user, permissions);
              adminService.addUser(user);

              return ResponseEntity.ok(user);
       }

}
