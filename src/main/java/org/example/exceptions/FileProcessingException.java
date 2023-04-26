package org.example.exceptions;

public class FileProcessingException extends RuntimeException{
    public FileProcessingException(String message) {
        super(message
        );
    }

    public FileProcessingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
