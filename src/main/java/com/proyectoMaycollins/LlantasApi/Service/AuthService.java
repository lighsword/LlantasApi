package com.proyectoMaycollins.LlantasApi.Service;

import com.proyectoMaycollins.LlantasApi.Model.Usuarios;
import com.proyectoMaycollins.LlantasApi.Model.enums.Role;
import com.proyectoMaycollins.LlantasApi.Repository.UsuariosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuariosRepository usuariosRepository;
    private final PasswordEncoder passwordEncoder;

    public Usuarios register(String email, String nombre, String password, Role rol) {
        if (usuariosRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email ya registrado");
        }
        Usuarios user = Usuarios.builder()
                .email(email)
                .nombre(nombre)
                .password(passwordEncoder.encode(password))
                .rol(rol)
                .activo(true)
                .build();
        return usuariosRepository.save(user);
    }

    public Optional<Usuarios> validateUser(String email, String rawPassword) {
        return usuariosRepository.findByEmail(email)
                .filter(u -> passwordEncoder.matches(rawPassword, u.getPassword()));
    }

    public Optional<Usuarios> findByEmail(String email) {
        return usuariosRepository.findByEmail(email);
    }
}
