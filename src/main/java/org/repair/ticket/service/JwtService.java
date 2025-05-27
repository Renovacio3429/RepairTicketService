    package org.repair.ticket.service;

    import lombok.AllArgsConstructor;
    import org.repair.ticket.configuration.security.jwt.JwtTokenProvider;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.stereotype.Service;

    @Service
    @AllArgsConstructor
    public class JwtService {
        private final JwtTokenProvider jwtTokenProvider;
        private final PasswordEncoder passwordEncoder;

        public boolean passwordEncoderMatch(String loginRequestPassword, String userPassword) {
            return passwordEncoder.matches(loginRequestPassword, userPassword);
        }

        public String generateToken(String userName, String roleName) {
            return jwtTokenProvider.generateToken(userName, roleName);
        }
    }
