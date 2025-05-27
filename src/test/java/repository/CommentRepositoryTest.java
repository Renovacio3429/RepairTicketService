package repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.repair.ticket.model.Comment;
import org.repair.ticket.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class CommentRepositoryTest extends AbstractPostgresContainerTest {

    @Autowired
    CommentRepository commentRepository;

    @BeforeEach
    void clear() {
        commentRepository.deleteAll();
    }

    @Test
    void testSaveAndFind() {
        var comment = Comment.builder()
                .text("Hello world")
                .build();
        comment = commentRepository.save(comment);

        assertThat(commentRepository.findById(comment.getId())).isPresent();
        assertThat(commentRepository.findAll()).extracting("text").contains("Hello world");
    }
}
