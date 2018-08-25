package com.example.my.mydiary.eventbus;

/**
 * @author ggz
 * @date 18年8月13日
 */

public class MessageEvent {
    private String message;

    public MessageEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
