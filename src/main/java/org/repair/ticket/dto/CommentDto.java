package org.repair.ticket.dto;


import lombok.Data;

@Data
public class CommentDto {
    private Long id;
    private String text;
    private String author;
    private String createdAt;
}
