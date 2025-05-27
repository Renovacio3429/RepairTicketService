package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.repair.ticket.model.OutboxEvent;
import org.repair.ticket.repository.OutboxEventRepository;
import org.repair.ticket.service.OutboxService;

import static org.assertj.core.api.Assertions.assertThat;


class OutboxServiceTest {
    OutboxEventRepository repo = Mockito.mock(OutboxEventRepository.class);
    ObjectMapper objectMapper = new ObjectMapper();
    OutboxService service = new OutboxService(repo, objectMapper);

    @Test
    void saveStatusChangedEvent_savesEvent() {
        var captor = ArgumentCaptor.forClass(OutboxEvent.class);
        service.saveStatusChangedEvent(5L, "IN_PROGRESS", "john");
        Mockito.verify(repo).save(captor.capture());
        assertThat(captor.getValue().getAggregateId()).isEqualTo("5");
        assertThat(captor.getValue().getType()).isEqualTo("STATUS_CHANGED");
    }
}