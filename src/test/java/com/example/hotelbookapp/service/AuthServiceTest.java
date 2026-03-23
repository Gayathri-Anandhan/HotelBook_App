package com.example.hotelbookapp.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import com.example.hotelbookapp.config.JwtUtil;
import com.example.hotelbookapp.entity.Login;
import com.example.hotelbookapp.repository.LoginDetailsRepo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private LoginDetailsRepo loginDetailsRepo;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private Login login;

    @BeforeEach
    void setUp() {
        login = new Login();
        login.setUsername("testuser");
        login.setEmail("test@example.com");
        login.setPassword("password123");
    }

    //  Test: Username already exists
    @Test
    void testSignUp_UsernameExists() {
        when(loginDetailsRepo.findByUsername("testuser"))
                .thenReturn(Optional.of(login));

        String result = authService.signUp(login);

        assertEquals("Username already exists", result);
    }

    //  Test: Email already exists
    @Test
    void testSignUp_EmailExists() {
        when(loginDetailsRepo.findByUsername("testuser"))
                .thenReturn(Optional.empty());

        when(loginDetailsRepo.findByEmail("test@example.com"))
                .thenReturn(Optional.of(login));

        String result = authService.signUp(login);

        assertEquals("Email already exists", result);
    }

    //  Test: Successful signup
    @Test
    void testSignUp_Success() {
        when(loginDetailsRepo.findByUsername("testuser"))
                .thenReturn(Optional.empty());

        when(loginDetailsRepo.findByEmail("test@example.com"))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode("password123"))
                .thenReturn("encodedPassword");

        String result = authService.signUp(login);

        assertEquals("User registered successfully", result);
        assertEquals("encodedPassword", login.getPassword());
        verify(loginDetailsRepo, times(1)).save(login);
    }

    //  Test: Default role assignment
    @Test
    void testSignUp_DefaultRole() {
        login.setRole(null);

        when(loginDetailsRepo.findByUsername(any()))
                .thenReturn(Optional.empty());

        when(loginDetailsRepo.findByEmail(any()))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode(any()))
                .thenReturn("encodedPassword");

        authService.signUp(login);

        assertEquals("USER", login.getRole());
    }

    //  Test: Successful signin
    @Test
    void testSignIn_Success() {
        Authentication authentication = mock(Authentication.class);

        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(authentication.getName()).thenReturn("testuser");

        when(jwtUtil.generateToken("testuser"))
                .thenReturn("mocked-jwt-token");

        String token = authService.signIn("testuser", "password123");

        assertEquals("mocked-jwt-token", token);
    }
}
