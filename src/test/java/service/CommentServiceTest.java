package service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.repair.ticket.model.Comment;
import org.repair.ticket.model.RepairRequest;
import org.repair.ticket.model.User;
import org.repair.ticket.repository.CommentRepository;
import org.repair.ticket.service.CommentService;
import org.repair.ticket.service.RepairRequestService;
import org.repair.ticket.service.UserService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

class CommentServiceTest {
    UserService userService = Mockito.mock(UserService.class);
    RepairRequestService repairRequestService = Mockito.mock(RepairRequestService.class);
    CommentRepository commentRepository = Mockito.mock(CommentRepository.class);

    CommentService commentService = new CommentService(commentRepository, userService, repairRequestService);

    @Test
    void addComment_happyPath() {
        User author = User.builder().id(2L).username("john").build();
        RepairRequest req = RepairRequest.builder().id(1L).build();
        Mockito.when(userService.findByUsername("john")).thenReturn(Optional.of(author));
        Mockito.when(repairRequestService.findById(1L)).thenReturn(req);
        Comment saved = Comment.builder().id(123L).build();
        Mockito.when(commentRepository.save(any(Comment.class))).thenReturn(saved);

        Long commentId = commentService.addComment(1L, "hello", "john");
        assertThat(commentId).isEqualTo(123L);
    }
}
