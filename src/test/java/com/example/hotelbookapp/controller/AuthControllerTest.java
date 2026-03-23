package com.example.hotelbookapp.controller;

import com.example.hotelbookapp.entity.Login;
import com.example.hotelbookapp.service.AuthService;
import com.example.hotelbookapp.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private AuthService authservice;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSignIn_Success() {
        Login login = new Login();
        login.setUsername("user1");
        login.setPassword("pass1");
        login.setRole("ADMIN");
        login.setName("John");

        when(authservice.signIn("user1", "pass1")).thenReturn("token123");
        when(userService.getUserByUsername("user1")).thenReturn(Optional.of(login));

        ResponseEntity<?> response = authController.signIn(login);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals("token123", body.get("token"));
        assertEquals("ADMIN", body.get("role"));
        assertEquals("John", body.get("name"));
    }

    @Test
    void testSignIn_BadCredentials() {
        Login login = new Login();
        login.setUsername("user1");
        login.setPassword("wrongpass");

        when(authservice.signIn("user1", "wrongpass"))
                .thenThrow(new BadCredentialsException("Invalid username or password"));

        ResponseEntity<?> response = authController.signIn(login);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals("Invalid username or password", body.get("error"));
    }

    @Test
    void testSignIn_UsernameNotFound() {
        Login login = new Login();
        login.setUsername("unknown");
        login.setPassword("pass");

        when(authservice.signIn("unknown", "pass"))
                .thenThrow(new UsernameNotFoundException("User not found"));

        ResponseEntity<?> response = authController.signIn(login);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals("User not found", body.get("error"));
    }

    @Test
    void testSignUp_Success() throws Exception {
        Login login = new Login();
        login.setUsername("newuser");
        login.setPassword("pass");
        login.setEmail("email@test.com");

        when(authservice.signUp(login)).thenReturn("User created");

        ResponseEntity<?> response = authController.signUp(login);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals("User created", body.get("message"));
    }

    @Test
    void testSignUp_Failure_MissingFields() {
        Login login = new Login();
        login.setUsername("user"); // password and email missing

        ResponseEntity<?> response = authController.signUp(login);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Username, email, and password are required", response.getBody());
    }

    @Test
    void testGetAllLogins() {
        List<Login> users = Arrays.asList(new Login(), new Login());
        when(userService.getAllUsers()).thenReturn(users);

        ResponseEntity<?> response = authController.getAllLogins();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, ((List<?>) response.getBody()).size());
    }

    @Test
    void testUpdateUser() {
        Login update = new Login();
        update.setName("Updated Name");

        when(userService.updateLogin(1L, update)).thenReturn(update);

        Login result = authController.updateProperty(1L, update);

        assertEquals("Updated Name", result.getName());
    }

    @Test
    void testDeleteUser_Success() {
        doNothing().when(userService).deleteLogin(1L);

        ResponseEntity<String> response = authController.deleteUser(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User deleted successfully with id: 1", response.getBody());
    }

    @Test
    void testDeleteUser_NotFound() {
        doThrow(new EntityNotFoundException("User not found")).when(userService).deleteLogin(99L);

        ResponseEntity<String> response = authController.deleteUser(99L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody());
    }

    @Test
    void testViewUser() {
        Login login = new Login();
        login.setUsername("user1");

        when(userService.getUserById(1L)).thenReturn(Optional.of(login));

        Optional<Login> response = authController.viewProperty(1L);

        assertTrue(response.isPresent());
        assertEquals("user1", response.get().getUsername());
    }
}