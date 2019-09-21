package com.flowdaq.app;

public class ApplicationException extends RuntimeException{

	private static final long serialVersionUID = 3276386932641430166L;

    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
