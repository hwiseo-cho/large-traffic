package com.fastcampus.boardserver.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter @Setter
public class BoerdServerException extends RuntimeException{
    HttpStatus status;
    String msg;
}
