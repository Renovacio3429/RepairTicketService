package org.repair.ticket.controller;

import lombok.RequiredArgsConstructor;
import org.openapitools.model.CommentCreateDto;
import org.openapitools.model.ResponseDetailsDto;
import org.openapitools.model.StatusDto;
import org.openapitools.model.TicketResponseDto;
import org.repair.ticket.annotation.CheckRole;
import org.repair.ticket.dictionary.Role;
import org.repair.ticket.service.CommentService;
import org.repair.ticket.service.RepairRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;
import org.openapitools.api.RepairRequestsApi;
import org.openapitools.model.TicketRequestDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RequestsController implements RepairRequestsApi {

    private final RepairRequestService repairRequestService;
    private final CommentService commentService;

    @Override
    @CheckRole(allowedRoles = {Role.ADMIN, Role.MANAGER, Role.TECHNICIAN})
    public ResponseEntity<List<TicketResponseDto>> apiRequestsGet() {
        var username = getCurrentUsername();
        var tickets = repairRequestService.getAllForUser(username);
        return ResponseEntity.ok(tickets);
    }

    @Override
    @CheckRole(allowedRoles = {Role.ADMIN})
    public ResponseEntity<Void> apiRequestsIdAssignUserIdPut(Long id, Long userId) {
        repairRequestService.assignUser(id, userId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Integer> apiRequestsIdCommentsPost(Long id, CommentCreateDto commentCreateDto) {
        var username = getCurrentUsername();
        var commentId = commentService.addComment(id, commentCreateDto.getText(), username);
        return ResponseEntity.ok(commentId.intValue());
    }

    @Override
    public ResponseEntity<ResponseDetailsDto> apiRequestsIdGet(Long id) {
        var details = repairRequestService.getDetails(id);
        if (details == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(details);
    }

    @Override
    @CheckRole(allowedRoles = {Role.ADMIN, Role.TECHNICIAN})
    public ResponseEntity<Void> apiRequestsIdStatusPut(Long id, StatusDto statusDto) {
        var username = getCurrentUsername();
        repairRequestService.updateStatus(id, statusDto, username);
        return ResponseEntity.noContent().build();
    }

    @Override
    @CheckRole(allowedRoles = {Role.ADMIN, Role.MANAGER})
    public ResponseEntity<TicketResponseDto> apiRequestsPost(TicketRequestDto ticketRequestDto) {
        var username = getCurrentUsername();
        var dto = repairRequestService.create(
                ticketRequestDto.getTitle(),
                ticketRequestDto.getDescription(),
                username
        );
        return ResponseEntity.ok(dto);
    }

    private String getCurrentUsername() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
