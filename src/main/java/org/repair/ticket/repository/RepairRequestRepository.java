package org.repair.ticket.repository;

import org.repair.ticket.model.RepairRequest;
import org.repair.ticket.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepairRequestRepository extends JpaRepository<RepairRequest, Long> {
    List<RepairRequest> findAllByCreatedBy(User manager);
    List<RepairRequest> findAllByAssignedTo(User technician);
}
