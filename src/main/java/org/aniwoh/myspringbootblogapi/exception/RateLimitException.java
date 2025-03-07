package org.aniwoh.myspringbootblogapi.exception;


import lombok.Getter;

@Getter
public class RateLimitException extends RuntimeException{
    private final String message;

    public RateLimitException(String message) {
        super(message);
        this.message = message;
    }

}
