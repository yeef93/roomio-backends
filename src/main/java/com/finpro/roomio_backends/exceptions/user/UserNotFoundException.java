package com.finpro.roomio_backends.exceptions.user;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
      super(message);
    }

    public UserNotFoundException(String message, Throwable cause) {
      super(message, cause);
    }
}
