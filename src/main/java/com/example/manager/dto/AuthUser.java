package com.example.manager.dto;

import com.example.manager.model.Role;

public record AuthUser(long userId, String accountName, Role role) {
    
}
