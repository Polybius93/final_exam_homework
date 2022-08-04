package com.example.final_exam_homework.unit;

import com.example.final_exam_homework.dtos.AuthenticationResponseDTO;
import com.example.final_exam_homework.dtos.UserRegistrationRequestDTO;
import com.example.final_exam_homework.dtos.UserRegistrationResponseDTO;
import com.example.final_exam_homework.models.User;
import com.example.final_exam_homework.repositories.UserRepository;
import com.example.final_exam_homework.services.UserService;
import com.example.final_exam_homework.services.UserServiceImpl;
import com.example.final_exam_homework.utils.JwtUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class UserServiceTest {

    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final AuthenticationManager authenticationManager = Mockito.mock(AuthenticationManager.class);
    private final JwtUtil jwtUtil = Mockito.mock(JwtUtil.class);
    private final ModelMapper modelMapper = new ModelMapper();
    private final UserService userService = new UserServiceImpl(userRepository, authenticationManager, jwtUtil, modelMapper);

    @Test
    public void findByUsername_withExistingUser_ShouldReturnUser() {
        User expectedUser = new User();
        expectedUser.setUsername("Zeno Petceran");
        Mockito.when(userRepository.findByUsername("Zeno Petceran")).thenReturn(Optional.of(expectedUser));

        User actualUser = userService.findUserByUsername("Zeno Petceran");

        Assertions.assertEquals(expectedUser, actualUser);
    }

    @Test
    public void findByUsername_withNonExistingUser_ShouldReturn() {
        User expectedUser = new User();
        expectedUser.setUsername("Zeno Petceran");
        String expectedExceptionMessage = "No user found by this username!";
        Mockito.when(userRepository.findByUsername("Zeno Petceran")).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(UsernameNotFoundException.class, () -> userService.findUserByUsername("Zeno Petceran"));

        Assertions.assertEquals(expectedExceptionMessage, exception.getMessage() );
    }

    @Test
    public void createAuthenticationResponseDTO_WithValidInformations_ShouldReturnAuthenticationResponseDTO() {
        User userTest = new User();
        userTest.setGreenBayDollars(1000L);
        Mockito.when(userRepository.findByUsername("Zeno Petceran")).thenReturn(Optional.of(userTest));

        AuthenticationResponseDTO actualAuthenticationResponseDTO = userService.createAuthenticationResponseDTO("Zeno Petceran", "87654321");

        Assertions.assertEquals("ok", actualAuthenticationResponseDTO.getStatus() );
        Assertions.assertEquals("87654321", actualAuthenticationResponseDTO.getJwt());
        Assertions.assertEquals(1000L , actualAuthenticationResponseDTO.getGreenBayDollars());
    }

    @Test
    public void registerUser_WithValidInformation_ShouldReturnUserRegistrationResponseDTO() {
        UserRegistrationRequestDTO userRegistrationRequestDTO = new UserRegistrationRequestDTO();
        userRegistrationRequestDTO.setUsername("Zeno Petceran");
        userRegistrationRequestDTO.setPassword("12345678");

        UserRegistrationResponseDTO userRegistrationResponseDTO = userService.registerUser(userRegistrationRequestDTO);

        Assertions.assertEquals("Zeno Petceran", userRegistrationResponseDTO.getUsername());
    }
}
