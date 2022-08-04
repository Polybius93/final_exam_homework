package com.example.final_exam_homework.services;

import com.example.final_exam_homework.dtos.AuthenticationRequestDTO;
import com.example.final_exam_homework.dtos.AuthenticationResponseDTO;
import com.example.final_exam_homework.dtos.UserRegistrationRequestDTO;
import com.example.final_exam_homework.dtos.UserRegistrationResponseDTO;
import com.example.final_exam_homework.models.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService {

    User findUserByUsername(String username);

    User findUserById(Long userId);

    AuthenticationResponseDTO createAuthenticationResponseDTO(String username, String jwt);

    void authenticateRequest(AuthenticationRequestDTO authenticationRequestDTO);

    UserRegistrationResponseDTO registerUser(UserRegistrationRequestDTO userRegistrationRequestDTO);
}
