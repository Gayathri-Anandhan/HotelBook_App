package com.example.hotelbookapp.service;

import com.example.hotelbookapp.entity.Login;
import com.example.hotelbookapp.repository.LoginDetailsRepo;

import jakarta.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private LoginDetailsRepo loginDetailsRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private Login user;

    @BeforeEach
    void setUp() {
        user = new Login();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("pass");
        user.setEmail("test@mail.com");
        user.setRole("USER");
    }

    //  Create user
    @Test
    void testCreateLogin() {
        when(loginDetailsRepo.save(user)).thenReturn(user);

        Login result = userService.createLogin(user);

        assertEquals("testuser", result.getUsername());
    }

    //  Get all users
    @Test
    void testGetAllUsers() {
        when(loginDetailsRepo.findAll()).thenReturn(Arrays.asList(user));

        List<Login> list = userService.getAllUsers();

        assertEquals(1, list.size());
    }

    //  Get user by ID
    @Test
    void testGetUserById() {
        when(loginDetailsRepo.findById(1L)).thenReturn(Optional.of(user));

        Optional<Login> result = userService.getUserById(1L);

        assertTrue(result.isPresent());
    }

    //  Get user by username
    @Test
    void testGetUserByUsername() {
        when(loginDetailsRepo.findByUsername("testuser"))
                .thenReturn(Optional.of(user));

        Optional<Login> result = userService.getUserByUsername("testuser");

        assertTrue(result.isPresent());
    }

    //  findByUsername - not found
    @Test
    void testFindByUsername_NotFound() {
        when(loginDetailsRepo.findByUsername("testuser"))
                .thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userService.findByUsername("testuser");
        });
    }

    //  Update user - success
    @Test
    void testUpdateLogin_Success() {
        Login updated = new Login();
        updated.setName("New Name");
        updated.setEmail("new@mail.com");
        updated.setUsername("newuser");
        updated.setPassword("newpass");
        updated.setRole("ADMIN");

        when(loginDetailsRepo.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newpass")).thenReturn("encodedPass");
        when(loginDetailsRepo.save(any())).thenReturn(user);

        Login result = userService.updateLogin(1L, updated);

        assertEquals("newuser", result.getUsername());
        assertEquals("encodedPass", result.getPassword());
    }

    //  Update user - not found
    @Test
    void testUpdateLogin_NotFound() {
        when(loginDetailsRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            userService.updateLogin(1L, new Login());
        });
    }

    //  Delete user - success
    @Test
    void testDeleteLogin_Success() {
        when(loginDetailsRepo.existsById(1L)).thenReturn(true);

        userService.deleteLogin(1L);

        verify(loginDetailsRepo, times(1)).deleteById(1L);
    }

    //  Delete user - not found
    @Test
    void testDeleteLogin_NotFound() {
        when(loginDetailsRepo.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> {
            userService.deleteLogin(1L);
        });
    }

    //  loadUserByUsername - success
    @Test
    void testLoadUserByUsername_Success() {
        when(loginDetailsRepo.findByUsername("testuser"))
                .thenReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserByUsername("testuser");

        assertEquals("testuser", userDetails.getUsername());
    }

    //  loadUserByUsername - not found
    @Test
    void testLoadUserByUsername_NotFound() {
        when(loginDetailsRepo.findByUsername("testuser"))
                .thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername("testuser");
        });
    }
}