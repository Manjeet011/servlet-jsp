package com.misha.payload.response;

public class MessageResponse {
    private  String message;
    public void setMessage(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
    public MessageResponse(String message) {
        this.message = message;
    }
}
