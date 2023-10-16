package com.delicoffee.deli.exception;

/**
 * 统一异常枚举类型
 */
public enum DeliCoffeeExceptionEnum {
    NEED_USER_NAME(10001, "Username is empty"),
    NEED_PASSWORD(10002, "Password is empty"),
    PASSWORD_TOO_SHORT(10003, "Password should be at least 8 characters"),
    NAME_EXISTED(10004, "Username existed"),
    INSERT_FAILED(10005, "Insert failed"),
    WRONG_PASSWORD(10006, "Wrong password"),
    NEED_LOGIN(10007, "Need login"),
    UPDATE_FAILED(10008, "Update failed"),
    NEED_ADMIN(10009, "Need admin role"),
    PARA_NOT_NULL(10010, "Parameter cannot be empty"),
    CREATE_FAILED(10011, "Create failed"),
    REQUEST_PARAM_ERROR(10012, "Wrong parameter"),
    DELETE_FAILED(10013, "Delete failed"),
    MKDIR_FAILED(10014, "File created failed"),
    UPLOAD_FAILED(10015, "Upload picture failed"),
    NOT_SALE(10016, "Product does not on sale"),
    NOT_ENOUGH(10017, "Under stock"),
    CART_EMPTY(10018, "No product in cart"),
    NO_ENUM(10019, "Cannot find the Enum"),
    NO_ORDER(10020, "Order does not exist"),
    NOT_YOUR_ORDER(10021, "Not your order"),
    WRONG_ORDER_STATUS(10022, "Wrong order status"),
    WRONG_MOBILE(10023, "Wrong phone number"),
    MOBILE_ALREADY_BEEN_REGISTERED(10024, "Phone number has been registered"),
    MOBILE_ALREADY_BEEN_SEND(10025, "Email has been sent, please wait or try later"),
    NEED_MOBILE(10026, "Need mobile phone number"),
    NEED_VERIFICATION_CODE(10027, "Verification code is empty"),
    WRONG_VERIFICATION_CODE(10028, "Wrong verification code"),
    TOKEN_EXPIRED(10029,"Token expired"),
    TOKEN_WRONG(10030,"Wrong token"),
    DOUBLE_ORDER(10031, "Not allow repeated orders by the same user");

    // exception code
    Integer code;

    // exception message
    String msg;

    DeliCoffeeExceptionEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
