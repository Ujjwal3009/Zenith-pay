package com.zenith.payment_gateway.service;

import com.zenith.payment_gateway.dto.LoginRequest;
import com.zenith.payment_gateway.dto.LoginResponse;
import com.zenith.payment_gateway.dto.SignupRequest;
import com.zenith.payment_gateway.dto.SignupResponse;
import com.zenith.payment_gateway.entity.Role;
import com.zenith.payment_gateway.entity.UserEntity;
import com.zenith.payment_gateway.exception.EmailAlreadyExistsException;
import com.zenith.payment_gateway.exception.InvalidCredentialsException;
import com.zenith.payment_gateway.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public SignupResponse signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already registered");
        }

        UserEntity user = UserEntity.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .build();

        userRepository.save(user);

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles("USER")
                .build();

        String token = jwtService.generateToken(userDetails);

        return new SignupResponse(token);
    }

    public LoginResponse login(LoginRequest request) {

        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        UserDetails springUser = org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name().replace("ROLE_", ""))
                .build();

        String token = jwtService.generateToken(springUser);

        return new LoginResponse(token);
    }



}
