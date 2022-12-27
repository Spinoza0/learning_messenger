package com.spinoza.messenger.data;

public class Message {
    private String text;
    private String senderId;
    private String receiverId;

    public Message(String text, String senderId, String receiverId) {
        this.text = text;
        this.senderId = senderId;
        this.receiverId = receiverId;
    }

    @SuppressWarnings("unused")
    public Message() {
    }

    public String getText() {
        return text;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }
}
