package com.sapu.movieflix.exceptions;


public class MovieDoesntExist extends RuntimeException{
    public MovieDoesntExist(String message){
        super(message);
    }
}
