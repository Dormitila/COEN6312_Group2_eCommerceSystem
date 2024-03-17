package com.coen6312.ecommerce.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Message {

    public enum MessageType {
        WARNING,
        COMPLAINT,
        OUT_OF_STOCK,
        WARNING_NEEDED
    }

    private MessageType type;
    private String content;
}
