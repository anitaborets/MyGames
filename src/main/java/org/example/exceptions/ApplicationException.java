package org.example.exceptions;

public class ApplicationException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final int code;

    public ApplicationException(int code, String message) {
        super(message);
        this.code = code;
    }

    public ApplicationException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
