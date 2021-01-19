package com.krgcorporate.postmanrunner.settings;

import org.springframework.http.HttpStatus;

public class Response<T> {

    public static <T> Response<T> ok(T data) {
        return new Response<>(HttpStatus.OK, data);
    }

    public static <T> Response<T> error(HttpStatus status, String error, String message) {
        return new Response<>(status, error, message);
    }

    private int status;

    private String code;

    private String message;

    private T data;

    @SuppressWarnings("unused")
    public Response() {
    }

    public Response(HttpStatus status) {
        this(status, null, null, null);
    }

    public Response(HttpStatus status, T data) {
        this(status, null, null, data);
    }

    public Response(HttpStatus status, String code, String message) {
        this(status, code, message, null);
    }

    public Response(HttpStatus status, String code, String message, T data) {
        this.status = status.value();
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Response{" +
                "status=" + status +
                ", code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
