package com.miro.widget.exception;

public class MissingParameterException extends Throwable {
    public MissingParameterException(String message) {
        super("Missing parameter : " + message);
    }
}
