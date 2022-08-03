package com.example.final_exam_homework.services;

import com.example.final_exam_homework.dtos.AuthenticationRequestDTO;
import com.example.final_exam_homework.dtos.AuthenticationResponseDTO;
import com.example.final_exam_homework.dtos.UserRegistrationRequestDTO;
import com.example.final_exam_homework.dtos.UserRegistrationResponseDTO;
import com.example.final_exam_homework.exceptions.*;
import com.example.final_exam_homework.models.User;
import com.example.final_exam_homework.repositories.UserRepository;
import com.example.final_exam_homework.utils.JwtUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, AuthenticationManager authenticationManager, JwtUtil jwtUtil, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.modelMapper = modelMapper;
    }

    @Override
    public void authenticateRequest(AuthenticationRequestDTO authenticationRequestDTO) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequestDTO.getUsername(), authenticationRequestDTO.getPassword()));
        } catch (BadCredentialsException e) {
            if (authenticationRequestDTO.getUsername() == null || authenticationRequestDTO.getUsername().isBlank()) {
                throw new MissingUsernameException("Username is missing!");
            } else if (authenticationRequestDTO.getPassword() == null || authenticationRequestDTO.getPassword().isBlank()) {
                throw new MissingPasswordException("Password is missing!");
            } else if (authenticationRequestDTO.getPassword().length() < 8) {
                throw new InvalidPasswordLengthException("Password length should be at least 8 characters!");
            } else if (userRepository.findByUsername(authenticationRequestDTO.getUsername()).isEmpty()) {
                throw new InvalidLoginCredentialsException("No user found by this username!");
            }
        }
    }

    public User findUserByUsername(String username) {
        if (userRepository.findByUsername(username).isPresent()) {
            return userRepository.findByUsername(username).get();
        } else {
            throw new UsernameNotFoundException("No user found by this username!");
        }
    }

    public AuthenticationResponseDTO createAuthenticationResponseDTO(String username, String jwt) {
        AuthenticationResponseDTO authenticationResponseDTO = new AuthenticationResponseDTO(jwt);
        Long greenbayDollars = findUserByUsername(username).getGreenBayDollars();
        authenticationResponseDTO.setGreenBayDollars(greenbayDollars);
        return authenticationResponseDTO;
    }

    private void validateUserRegistrationRequest(UserRegistrationRequestDTO userRegistrationRequestDTO) {
        if ((userRegistrationRequestDTO.getUsername() == null || userRegistrationRequestDTO.getUsername().isBlank())
                && (userRegistrationRequestDTO.getPassword() == null || userRegistrationRequestDTO.getPassword().isBlank())) {
            throw new MissingUsernameAndPasswordException("Username and password is missing!");
        } else if (userRegistrationRequestDTO.getUsername() == null || userRegistrationRequestDTO.getUsername().isBlank()) {
            throw new MissingUsernameException("Username is missing!");
        } else if (userRepository.findByUsername(userRegistrationRequestDTO.getUsername()).isPresent()) {
            throw new UsernameAlreadyInUseException("Username already in use!");
        } else if (userRegistrationRequestDTO.getPassword() == null || userRegistrationRequestDTO.getPassword().isBlank()) {
            throw new MissingPasswordException("Password is missing!");
        } else if (userRegistrationRequestDTO.getPassword().length() < 8) {
            throw new InvalidPasswordLengthException("Password length should be at least 8 characters!");
        }
    }

    @Override
    public UserRegistrationResponseDTO registerUser(UserRegistrationRequestDTO userRegistrationRequestDTO) {
        UserRegistrationResponseDTO userRegistrationResponseDTO = new UserRegistrationResponseDTO();
        validateUserRegistrationRequest(userRegistrationRequestDTO);
        User user = modelMapper.map(userRegistrationRequestDTO, User.class);
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(11);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setGreenBayDollars(0L);
        userRepository.save(user);
        modelMapper.map(user, userRegistrationResponseDTO);
        return userRegistrationResponseDTO;
    }
}
