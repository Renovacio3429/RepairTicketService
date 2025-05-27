package controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.repair.ticket.controller.AuthController;
import org.repair.ticket.model.User;
import org.repair.ticket.service.JwtService;
import org.repair.ticket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerWebTest {
    @Autowired
    MockMvc mvc;
    @MockBean
    UserService userService;
    @MockBean
    JwtService jwtService;

    @Test
    void apiAuthLoginPost_success() throws Exception {
        User user = User.builder().username("a").password("pass").build();
        Mockito.when(userService.findByUsername("a")).thenReturn(Optional.of(user));
        Mockito.when(jwtService.passwordEncoderMatch("pass", "pass")).thenReturn(true);
        Mockito.when(jwtService.generateToken("a", null)).thenReturn("jwt");

        mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"a\", \"password\":\"pass\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt"));
    }
}
