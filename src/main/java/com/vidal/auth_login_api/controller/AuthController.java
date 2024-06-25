package com.vidal.auth_login_api.controller;

import com.vidal.auth_login_api.domain.User;
import com.vidal.auth_login_api.dto.LoginRequestDTO;
import com.vidal.auth_login_api.dto.ResponseDTO;
import com.vidal.auth_login_api.dto.RegisterRequestDTO;
import com.vidal.auth_login_api.infra.security.TokenService;
import com.vidal.auth_login_api.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.Optional;

/**
 * @author Fhellipe Vidal Rabelo
 * @since 2024-06-24
 */
@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequestDTO body){
        User user = this.repository.findByEmail(body.email()).orElseThrow(() -> new RuntimeException("User not found"));
        if(passwordEncoder.matches(body.password(), user.getPassword())) {
            String token = this.tokenService.generateToken(user);
            return ResponseEntity.ok(new ResponseDTO(user.getName(), token));
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping(path = "/register")
    public ResponseEntity register(@RequestBody RegisterRequestDTO loginInput) {
        Optional<User> user = repository.findByEmail(loginInput.email());
        if (user.isEmpty()) {
            User newUser = new User();
            newUser.setEmail(loginInput.email());
            newUser.setName(loginInput.name());
            newUser.setPassword(passwordEncoder.encode(loginInput.password()));
            repository.save(newUser);

            String token = tokenService.generateToken(newUser);
            return ResponseEntity.ok(new ResponseDTO(newUser.getName(), token));
        }

        return ResponseEntity.badRequest().build();
    }

}
