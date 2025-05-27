package service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.repair.ticket.configuration.security.jwt.JwtTokenProvider;
import org.repair.ticket.service.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {
    JwtTokenProvider tokenProvider = Mockito.mock(JwtTokenProvider.class);
    PasswordEncoder encoder = Mockito.mock(PasswordEncoder.class);

    JwtService jwtService = new JwtService(tokenProvider, encoder);

    @Test
    void passwordEncoderMatch() {
        Mockito.when(encoder.matches("plain", "encoded")).thenReturn(true);
        assertThat(jwtService.passwordEncoderMatch("plain", "encoded")).isTrue();
    }
}
