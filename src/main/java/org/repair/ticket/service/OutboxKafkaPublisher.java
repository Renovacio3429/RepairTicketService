package org.repair.ticket.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.repair.ticket.model.OutboxEvent;
import org.repair.ticket.repository.OutboxEventRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxKafkaPublisher {

    private final OutboxEventRepository outboxEventRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(fixedDelay = 2000)
    public void publishEvents() {
        List<OutboxEvent> events = outboxEventRepository.findAllByProcessedFalse();
        for (OutboxEvent event : events) {
            try {
                kafkaTemplate.send("repair-request-events", event.getPayload()).get();

                event.setProcessed(true);
                outboxEventRepository.save(event);

                log.info("Outbox event sent: id={}, type={}, aggregateId={}, processed={}",
                        event.getId(), event.getType(), event.getAggregateId(), event.isProcessed());
            } catch (Exception ex) {
                log.error("Failed to publish outbox event: id={}, type={}, aggregateId={}, error={}",
                        event.getId(), event.getType(), event.getAggregateId(), ex.getMessage(), ex);
            }
        }
    }
}
