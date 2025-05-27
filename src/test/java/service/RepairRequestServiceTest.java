package service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.repair.ticket.dictionary.Role;
import org.repair.ticket.mapper.RepairRequestMapper;
import org.repair.ticket.model.User;
import org.repair.ticket.repository.RepairRequestRepository;
import org.repair.ticket.service.OutboxService;
import org.repair.ticket.service.RepairRequestService;
import org.repair.ticket.service.UserService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class RepairRequestServiceTest {
    RepairRequestRepository repo = Mockito.mock(RepairRequestRepository.class);
    UserService userService = Mockito.mock(UserService.class);
    RepairRequestMapper mapper = Mockito.mock(RepairRequestMapper.class);
    OutboxService outboxService = Mockito.mock(OutboxService.class);
    RepairRequestService service = new RepairRequestService(repo, userService, mapper, outboxService);

    @Test
    void getAllForUser_manager() {
        User manager = User.builder().id(1L).username("m").role(Role.MANAGER).build();
        Mockito.when(userService.findByUsername("m")).thenReturn(Optional.of(manager));
        Mockito.when(repo.findAllByCreatedBy(manager)).thenReturn(List.of());
        assertThat(service.getAllForUser("m")).isEmpty();
    }
}
