package org.repair.ticket.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.repair.ticket.model.Comment;
import org.repair.ticket.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Slf4j
@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final RepairRequestService repairRequestService;

    public Long addComment(Long requestId, String text, String username) {
        var author = userService.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
        var repairRequest = repairRequestService.findById(requestId);

        var comment = Comment.builder()
                .text(text)
                .createdAt(OffsetDateTime.now())
                .author(author)
                .build();

        repairRequest.addComment(comment);
        var savedComment = commentRepository.save(comment);

        log.info("Comment {} added to request {}", savedComment.getId(), requestId);

        return savedComment.getId();
    }
}
