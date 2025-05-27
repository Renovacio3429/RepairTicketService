package org.repair.ticket.controller;

import lombok.RequiredArgsConstructor;
import org.repair.ticket.service.JwtService;
import org.repair.ticket.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.openapitools.model.AuthResponseDto;
import org.openapitools.api.AuthApi;
import org.openapitools.model.LoginRequestDto;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {
    private final UserService userService;
    private final JwtService jwtService;

    @Override
    public ResponseEntity<AuthResponseDto> apiAuthLoginPost(LoginRequestDto loginRequestDto) {
        return userService.findByUsername(loginRequestDto.getUsername())
                .filter(user -> jwtService.passwordEncoderMatch(loginRequestDto.getPassword(), user.getPassword()))
                .map(user -> {
                    String token = jwtService.generateToken(user.getUsername(), String.valueOf(user.getRole()));
                    return ResponseEntity.ok(new AuthResponseDto(token));
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
}
