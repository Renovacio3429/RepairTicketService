package org.repair.ticket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusChangedEventDto implements Serializable {
    private Long requestId;
    private String status;
    private String username;
    private Instant timestamp;
}
