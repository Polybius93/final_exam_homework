package com.example.final_exam_homework.controllers;

import com.example.final_exam_homework.dtos.AuthenticationRequestDTO;
import com.example.final_exam_homework.dtos.UserRegistrationRequestDTO;
import com.example.final_exam_homework.services.UserDetailsServiceImpl;
import com.example.final_exam_homework.services.UserService;
import com.example.final_exam_homework.utils.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class UserController {

    private final UserDetailsServiceImpl userDetailsService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public UserController(UserDetailsServiceImpl userDetailsService, UserService userService, JwtUtil jwtUtil) {
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registerUser(
            @RequestBody UserRegistrationRequestDTO userRegistrationRequestDTO) {
        return ResponseEntity.ok(userService.registerUser(userRegistrationRequestDTO));
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> loginUser(
            @RequestBody AuthenticationRequestDTO authenticationRequestDTO) {
        userService.authenticateRequest(authenticationRequestDTO);
        final UserDetails userDetails =
                userDetailsService.loadUserByUsername((authenticationRequestDTO.getUsername()));
        final String jwt = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(userService.createAuthenticationResponseDTO(userDetails.getUsername(), jwt));
    }
}