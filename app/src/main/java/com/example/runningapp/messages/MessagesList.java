package com.example.runningapp.messages;

public class MessagesList {

    private final String chatKey, fullName, username, lastMessage;
    private int unseenMessages;

    public MessagesList(String chatKey, String fullName, String username, String lastMessage, int unseenMessages) {
        this.chatKey = chatKey;
        this.fullName = fullName;
        this.username = username;
        this.lastMessage = lastMessage;
        this.unseenMessages = unseenMessages;
    }

    public String getChatKey() {
        return chatKey;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUsername() {
        return username;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public int getUnseenMessages() {
        return unseenMessages;
    }

    public void setUnseenMessages(int unseenMessages) {
        this.unseenMessages = unseenMessages;
    }
}
