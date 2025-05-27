package org.repair.ticket.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.repair.ticket.dto.StatusChangedEventDto;
import org.repair.ticket.model.OutboxEvent;
import org.repair.ticket.repository.OutboxEventRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OutboxService {

    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    public void saveStatusChangedEvent(Long requestId, String status, String username) {
        var eventDto = new StatusChangedEventDto(requestId, status, username, Instant.now());

        String payload;
        try {
            payload = objectMapper.writeValueAsString(eventDto);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize event DTO", e);
        }

        var outboxEvent = OutboxEvent.builder()
                .aggregateType("RepairRequest")
                .aggregateId(requestId.toString())
                .type("STATUS_CHANGED")
                .payload(payload)
                .createdAt(LocalDateTime.now())
                .processed(false)
                .build();

        outboxEventRepository.save(outboxEvent);
    }
}