package com.fastcampus.boardserver.dto;

import lombok.*;

@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CommentDTO {

    private int id;
    private int postId;
    private String contents;
    private int subCommentId;
}
