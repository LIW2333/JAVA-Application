package com.chatproj;

import java.io.Serializable;

public class MessageModel implements Serializable{
    private String type; // personal or group or else
    private String source;
    private String reciver;
    private String content;
    private Long Time;

    public MessageModel(){

    }

    public MessageModel(String sender, String getter, String content, Long time) {
        this.source = sender;
        reciver = getter;
        this.content = content;
        Time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String sender) {
        source = sender;
    }

    public String getReciver() {
        return reciver;
    }

    public void setReciver(String getter) {
        reciver = getter;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getTime() {
        return Time;
    }

    public void setTime(Long time) {
        Time = time;
    }
}
