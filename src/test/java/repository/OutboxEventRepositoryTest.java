package repository;

import org.junit.jupiter.api.Test;
import org.repair.ticket.model.OutboxEvent;
import org.repair.ticket.repository.OutboxEventRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class OutboxEventRepositoryTest extends AbstractPostgresContainerTest {


    @Autowired
    OutboxEventRepository outboxEventRepository;

    @Test
    void testFindAllByProcessedFalse() {
        var event1 = OutboxEvent.builder()
                .payload("event1")
                .processed(false)
                .createdAt(LocalDateTime.now())
                .build();

        var event2 = OutboxEvent.builder()
                .payload("event2")
                .processed(true)
                .createdAt(LocalDateTime.now())
                .build();

        outboxEventRepository.save(event1);
        outboxEventRepository.save(event2);

        var notProcessed = outboxEventRepository.findAllByProcessedFalse();
        assertThat(notProcessed).extracting("payload").contains("event1");
        assertThat(notProcessed).noneMatch(OutboxEvent::isProcessed);
    }
}
