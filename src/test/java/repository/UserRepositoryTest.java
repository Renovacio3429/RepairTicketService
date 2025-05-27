package repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.repair.ticket.dictionary.Role;
import org.repair.ticket.model.User;
import org.repair.ticket.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class UserRepositoryTest extends AbstractPostgresContainerTest {

    @Autowired
    UserRepository userRepository;


    @BeforeEach
    void cleanup() {
        userRepository.deleteAll();
    }
    @Test
    void testSaveAndFindByUsername() {
        User user = User.builder()
                .username("testuser")
                .password("secret")
                .role(Role.MANAGER)
                .build();
        userRepository.save(user);

        Optional<User> loaded = userRepository.findByUsername("testuser");
        assertThat(loaded).isPresent();
        assertThat(loaded.get().getUsername()).isEqualTo("testuser");
    }
}
