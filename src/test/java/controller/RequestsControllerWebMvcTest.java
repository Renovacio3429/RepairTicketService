package controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.repair.ticket.controller.RequestsController;
import org.repair.ticket.service.CommentService;
import org.repair.ticket.service.RepairRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RequestsController.class)
class RequestsControllerWebMvcTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    RepairRequestService repairRequestService;

    @MockBean
    CommentService commentService;

    @Test
    @WithMockUser(username = "manager", authorities = {"ROLE_MANAGER"})
    void apiRequestsGet_returnsTicketsForManager() throws Exception {
        var ticket = new org.openapitools.model.TicketResponseDto();
        ticket.setId(1L);
        ticket.setTitle("Test ticket");
        when(repairRequestService.getAllForUser("manager")).thenReturn(List.of(ticket));

        mvc.perform(get("/api/requests"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    @WithMockUser(username = "tech", authorities = {"ROLE_TECHNICIAN"})
    void apiRequestsIdStatusPut_okForTechnician() throws Exception {
        var statusDto = new org.openapitools.model.StatusDto();
        statusDto.setStatus(org.openapitools.model.Status.IN_PROGRESS);

        mvc.perform(put("/api/requests/123/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"IN_PROGRESS\"}"))
                .andExpect(status().isNoContent());

        Mockito.verify(repairRequestService).updateStatus(eq(123L), any(org.openapitools.model.StatusDto.class), eq("tech"));
    }

    @Test
    @WithMockUser(username = "manager", authorities = {"ROLE_MANAGER"})
    void apiRequestsPost_createTicketForManager() throws Exception {
        var request = new org.openapitools.model.TicketRequestDto();
        request.setTitle("New");
        request.setDescription("Desc");

        var response = new org.openapitools.model.TicketResponseDto();
        response.setId(77L);
        when(repairRequestService.create(any(), any(), eq("manager"))).thenReturn(response);

        mvc.perform(post("/api/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"New\", \"description\":\"Desc\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(77));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    void apiRequestsIdAssignUserIdPut_okForAdmin() throws Exception {
        mvc.perform(put("/api/requests/1/assign/2"))
                .andExpect(status().isNoContent());

        Mockito.verify(repairRequestService).assignUser(1L, 2L);
    }

    @Test
    @WithMockUser(username = "manager", authorities = {"ROLE_MANAGER"})
    void apiRequestsIdAssignUserIdPut_forbiddenForManager() throws Exception {
        mvc.perform(put("/api/requests/1/assign/2"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    void apiRequestsIdCommentsPost_createComment() throws Exception {
        when(commentService.addComment(eq(10L), eq("Some comment"), eq("admin"))).thenReturn(99L);

        mvc.perform(post("/api/requests/10/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"text\":\"Some comment\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("99"));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    void apiRequestsIdGet_found() throws Exception {
        var details = new org.openapitools.model.ResponseDetailsDto();
        details.setId(11L);
        details.setTitle("Details");
        when(repairRequestService.getDetails(11L)).thenReturn(details);

        mvc.perform(get("/api/requests/11"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(11));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    void apiRequestsIdGet_notFound() throws Exception {
        when(repairRequestService.getDetails(12L)).thenReturn(null);

        mvc.perform(get("/api/requests/12"))
                .andExpect(status().isNotFound());
    }
}
