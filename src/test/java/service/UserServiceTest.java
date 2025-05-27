package service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.repair.ticket.model.User;
import org.repair.ticket.repository.UserRepository;
import org.repair.ticket.service.UserService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class UserServiceTest {

    UserRepository userRepository = Mockito.mock(UserRepository.class);
    UserService userService = new UserService(userRepository);

    @Test
    void findByUsername_found() {
        var user = User.builder().id(1L).username("u1").build();
        Mockito.when(userRepository.findByUsername("u1")).thenReturn(Optional.of(user));
        assertThat(userService.findByUsername("u1")).contains(user);
    }
}
