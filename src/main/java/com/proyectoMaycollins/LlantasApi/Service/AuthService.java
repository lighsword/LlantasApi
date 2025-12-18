package com.proyectoMaycollins.LlantasApi.Service;

import com.proyectoMaycollins.LlantasApi.DTO.JwtAuthenticationResponse;
import com.proyectoMaycollins.LlantasApi.DTO.LoginRequest;
import com.proyectoMaycollins.LlantasApi.DTO.RegisterRequest;
import com.proyectoMaycollins.LlantasApi.Model.User;
import com.proyectoMaycollins.LlantasApi.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public JwtAuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        
        user = userRepository.save(user);
        var token = jwtService.generateToken(user);
        
        return JwtAuthenticationResponse.builder()
                .token(token)
                .build();
    }

    public JwtAuthenticationResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        
        @NonNull User user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        var token = jwtService.generateToken(user);
        
        return JwtAuthenticationResponse.builder()
                .token(token)
                .build();
    }
}