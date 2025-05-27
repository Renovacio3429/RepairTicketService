package service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.repair.ticket.model.OutboxEvent;
import org.repair.ticket.repository.OutboxEventRepository;
import org.repair.ticket.service.OutboxKafkaPublisher;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;

class OutboxKafkaPublisherTest {
    OutboxEventRepository repo = Mockito.mock(OutboxEventRepository.class);
    KafkaTemplate<String, String> kafkaTemplate = Mockito.mock(KafkaTemplate.class);
    OutboxKafkaPublisher publisher = new OutboxKafkaPublisher(repo, kafkaTemplate);

    @Test
    void publishEvents_sendsAndMarksProcessed() throws Exception {
        var event = OutboxEvent.builder()
                .id(1L).payload("pl").processed(false).build();
        Mockito.when(repo.findAllByProcessedFalse()).thenReturn(List.of(event));
        Mockito.when(kafkaTemplate.send(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(CompletableFuture.completedFuture(null));
        Mockito.when(repo.save(event)).thenReturn(event);

        publisher.publishEvents();
        Mockito.verify(kafkaTemplate).send("repair-request-events", "pl");
        Mockito.verify(repo).save(event);
    }
}