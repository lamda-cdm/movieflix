package com.sapu.movieflix.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MovieDoesntExist.class)
    public ProblemDetail handleMovieDoesntExist(MovieDoesntExist movieDoesntExist){
        // we can also send response enityt
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, movieDoesntExist.getMessage()
        );
        return problemDetail;
    }
    @ExceptionHandler(EmptyFileException.class)
    public ProblemDetail handleEmptyFileException(EmptyFileException emptyFileException){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, emptyFileException.getMessage()
        );
        return problemDetail;
    }
}
