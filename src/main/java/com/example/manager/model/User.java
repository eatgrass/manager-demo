package com.example.manager.model;

import java.util.Set;
import java.util.HashSet;
import java.util.Objects;

public record User(
        long userId,
        String accountName,
        Role role,
        Set<Permission> permissions) {

    public User(long userId, String accountName, Role role, Set<Permission> permissions) {
        this.userId = userId;
        this.accountName = accountName;
        this.role = role;
        this.permissions = permissions;
    }

    public User(long userId, Role role, Set<Permission> permissions) {
        this(userId, String.valueOf(userId), role, permissions);
    }

    public static User deserialize(String line) {

        String[] parts = line.split("\\|");
        var userId = Long.valueOf(parts[1]);
        var accountName = parts[2];
        var role = Role.valueOf(parts[3]);
        Set<Permission> permissions = new HashSet<>();
        for (int i = 4; i < parts.length; i++) {
            permissions.add(Permission.parse(parts[i]));
        }

        return new User(userId, accountName, role, permissions);

    }

    public String serialize() {
        var sb = new StringBuilder("user");
        sb.append("|");
        sb.append(userId);
        sb.append("|");
        sb.append(accountName);
        sb.append("|");
        sb.append(role.name());
        for (Permission perm : permissions) {
            sb.append("|");
            sb.append(perm);
        }

        return sb.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, accountName, role, permissions);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        User other = (User) obj;
        return userId == other.userId &&
                Objects.equals(accountName, other.accountName) &&
                role == other.role &&
                Objects.equals(permissions, other.permissions);
    }

}
