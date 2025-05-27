package org.repair.ticket.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.model.ResponseDetailsDto;
import org.openapitools.model.StatusDto;
import org.openapitools.model.TicketResponseDto;
import org.repair.ticket.dictionary.Role;
import org.repair.ticket.dictionary.Status;
import org.repair.ticket.mapper.RepairRequestMapper;
import org.repair.ticket.model.RepairRequest;
import org.repair.ticket.repository.RepairRequestRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import static org.openapitools.model.Status.DONE;
import static org.openapitools.model.Status.IN_PROGRESS;

@Slf4j
@Service
@RequiredArgsConstructor
public class RepairRequestService {
    private final RepairRequestRepository repairRequestRepository;
    private final UserService userService;
    private final RepairRequestMapper repairRequestMapper;
    private final OutboxService outboxService;

    public TicketResponseDto create(String title, String description, String username) {
        var user = userService.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("User not found: " + username));

        var request = RepairRequest.builder()
                .title(title)
                .description(description)
                .status(Status.NEW)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .createdBy(user)
                .build();

        var savedRequest = repairRequestRepository.save(request);
        log.info("Created RepairRequest: {}", savedRequest);

        return repairRequestMapper.toDto(savedRequest);
    }

    public List<TicketResponseDto> getAllForUser(String username) {
        var user = userService.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("User not found: " + username));
        var role = user.getRole();

        var requests = switch (role) {
            case Role.MANAGER -> repairRequestRepository.findAllByCreatedBy(user);
            case Role.TECHNICIAN -> repairRequestRepository.findAllByAssignedTo(user);
            case Role.ADMIN -> repairRequestRepository.findAll();
        };

        log.info("Fetched {} RepairRequests for user {} with role {}", requests.size(), username, role);

        return requests.stream()
                .map(repairRequestMapper::toDto)
                .toList();
    }

    @Transactional
    public void assignUser(Long requestId, Long userId) {
        var request = findById(requestId);
        var user = userService.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found: " + userId));

        request.setAssignedTo(user);
        request.setUpdatedAt(OffsetDateTime.now());
        repairRequestRepository.save(request);

        log.info("User {} assigned as master to request {}", userId, requestId);
    }

    public ResponseDetailsDto getDetails(Long requestId) {
        return repairRequestRepository.findById(requestId)
                .map(repairRequestMapper::toDetailsDto)
                .orElse(null);
    }

    @Transactional
    public void updateStatus(Long requestId, StatusDto statusDto, String username) {
        var request = findById(requestId);
        var user = userService.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("User not found: " + username));

        if (request.getAssignedTo() == null || !request.getAssignedTo().getId().equals(user.getId())) {
            throw new SecurityException("Not your assigned request");
        }

        var newStatus = statusDto.getStatus();
        request.setStatus(Status.valueOf(newStatus.getValue()));
        request.setUpdatedAt(OffsetDateTime.now());
        repairRequestRepository.save(request);

        log.info("Updated request {} to status {}", requestId, newStatus.name());

        if (newStatus == IN_PROGRESS || newStatus == DONE) {
            outboxService.saveStatusChangedEvent(request.getId(), newStatus.name(), username);
        }
    }


    public RepairRequest findById(Long id) {
        return repairRequestRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("RepairRequest not found: " + id));
    }
}

