package com.example.manager;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Base64;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.manager.controller.AdminController;
import com.example.manager.controller.UserController;
import com.example.manager.dto.AddUserRequest;
import com.example.manager.dto.AuthUser;
import com.example.manager.model.Permission;
import com.example.manager.model.Role;
import com.example.manager.model.User;
import com.example.manager.service.AdminService;
import com.example.manager.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class ManagerApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AdminService adminService;

    @MockBean
    private UserService userService;

    @InjectMocks
    private AdminController adminController;

    @InjectMocks
    private UserController userController;

    @Value("${server.tomcat.accesslog.directory}")
    private String log;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void accessResourceWithInvalidTokenShouldRespondUnauthorized() throws Exception {

        mockMvc.perform(post("/admin/addUser")
                .header("Authorization", "invalid token")
                .contentType("application/json"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/user/path")
                .header("Authorization", "invalid token"))
                .andExpect(status().isUnauthorized());

    }

    @Test
    void validAdminShouldSuccessfullyAddUser() throws Exception {

        doNothing().when(adminService).addUser(any(User.class));

        AuthUser authUser = new AuthUser(123, "David", Role.admin);
        String token = Base64.getEncoder().encodeToString(objectMapper.writeValueAsString(authUser).getBytes());
        AddUserRequest req = new AddUserRequest(256L, new String[] { "resource 1", "resource a" });
        mockMvc.perform(post("/admin/addUser")
                .header("Authorization", token)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }

    @Test
    void checkUserWithInvalidIdShouldRespondUnauthorized() throws Exception {
        when(userService.findUserById(anyLong())).thenReturn(null);
        AuthUser authUser = new AuthUser(1, "Lucy", Role.user);
        String token = Base64.getEncoder().encodeToString(objectMapper.writeValueAsString(authUser).getBytes());
        mockMvc.perform(get("/user/path")
                .header("Authorization", token))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void userAccessResourceWithPermissionShouldRespondSuccess() throws Exception {

        Set<Permission> perms = Set.of(new Permission("path"));
        User user = new User(1L, Role.user, perms);
        when(userService.findUserById(anyLong())).thenReturn(user);
        AuthUser authUser = new AuthUser(1, "Lucy", Role.user);
        String token = Base64.getEncoder().encodeToString(objectMapper.writeValueAsString(authUser).getBytes());

        mockMvc.perform(get("/user/path")
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().string("success"));
    }

    @Test
    void userAccessResourceWithoutPermissionShouldRespondFail() throws Exception {

        Set<Permission> perms = Set.of(new Permission("path"));
        User user = new User(1L, Role.user, perms);
        when(userService.findUserById(anyLong())).thenReturn(user);
        AuthUser authUser = new AuthUser(1, "Lucy", Role.user);
        String token = Base64.getEncoder().encodeToString(objectMapper.writeValueAsString(authUser).getBytes());

        mockMvc.perform(get("/user/test")
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().string("fail"));
    }

}
