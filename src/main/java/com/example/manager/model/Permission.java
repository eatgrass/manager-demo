package com.example.manager.model;

import java.util.Objects;

import com.example.manager.exception.ParameterException;
import com.example.manager.utils.StringUtils;

public class Permission {

    private String id;

    private String type;

    private Permission(String type, String id) {
        this.id = id;
        this.type = type;
    }

    public Permission(String id) {
        this.type = "resource";
        this.id = id;
    }

    public String getEndPoint() {
        return "/user/" + id;
    }

    public String getType() {
        return type;
    }

    public static Permission parse(String enpoint) {

        String endpoint = enpoint.trim();

        if (StringUtils.isEmpty(enpoint)) {
            throw new ParameterException("invalid endpoint");
        }

        String[] parts = endpoint.split(" ");

        if (parts.length != 2) {
            throw new ParameterException("illegal endpoint format: " + enpoint);
        }

        if (!"resource".equals(parts[0])) {
            throw new ParameterException("unsupported endpoint type: " + parts[0]);
        }

        return new Permission(parts[0], parts[1]);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Permission other = (Permission) obj;
        return Objects.equals(id, other.id) && Objects.equals(type, other.type);
    }

    public String toString() {
        return type + " " + id;
    }

}
