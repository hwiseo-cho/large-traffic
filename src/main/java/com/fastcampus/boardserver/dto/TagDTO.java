package com.fastcampus.boardserver.dto;

import lombok.*;

@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TagDTO {
    private int id;
    private String name;
    private String url;
    private int postId;

}
