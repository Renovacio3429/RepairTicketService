package org.repair.ticket.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.repair.ticket.dto.CommentDto;
import org.repair.ticket.model.Comment;
import org.repair.ticket.model.User;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "author", source = "author.username")
    @Mapping(target = "createdAt", source = "createdAt", qualifiedByName = "formatDateTime")
    CommentDto toDto(Comment comment);

    List<CommentDto> toDtoList(List<Comment> comments);

    default String map(User user) {
        return user == null ? null : user.getUsername();
    }

    @Named("formatDateTime")
    static String formatDateTime(java.time.OffsetDateTime dateTime) {
        if (dateTime == null) return null;
        return dateTime.format(DateTimeFormatter.ISO_DATE_TIME);
    }
}
