package com.delicoffee.deli.exception;

/**
 * 统一异常
 */
public class DeliCoffeeException extends RuntimeException {

    private final Integer code;
    private final String message;

    public DeliCoffeeException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public DeliCoffeeException(DeliCoffeeExceptionEnum exceptionEnum) {
        this(exceptionEnum.getCode(), exceptionEnum.getMsg());
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
