package repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.repair.ticket.dictionary.Role;
import org.repair.ticket.model.RepairRequest;
import org.repair.ticket.model.User;
import org.repair.ticket.repository.RepairRequestRepository;
import org.repair.ticket.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RepairRequestRepositoryTest extends AbstractPostgresContainerTest {


    @Autowired
    UserRepository userRepository;
    @Autowired
    RepairRequestRepository repairRequestRepository;

    User manager;
    User technician;

    @BeforeEach
    void setupUsers() {
        repairRequestRepository.deleteAll();
        userRepository.deleteAll();
        manager = userRepository.save(User.builder().username("manager").password("m123").role(Role.MANAGER).build());
        technician = userRepository.save(User.builder().username("tech").password("t123").role(Role.TECHNICIAN).build());
    }

    @Test
    void testFindAllByCreatedBy() {
        RepairRequest rr = RepairRequest.builder().createdBy(manager).assignedTo(technician).build();
        repairRequestRepository.save(rr);

        List<RepairRequest> found = repairRequestRepository.findAllByCreatedBy(manager);
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getCreatedBy().getUsername()).isEqualTo("manager");
    }

    @Test
    void testFindAllByAssignedTo() {
        RepairRequest rr = RepairRequest.builder().createdBy(manager).assignedTo(technician).build();
        repairRequestRepository.save(rr);

        List<RepairRequest> found = repairRequestRepository.findAllByAssignedTo(technician);
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getAssignedTo().getUsername()).isEqualTo("tech");
    }
}
