package org.eclipse.uprotocol.mqtt.data;

import jakarta.persistence.Entity;

@Entity
public class Message extends AbstractEntity {
    String message;
    String source;
    String sink;

    public Message() {
    }

    public Message(final String message, final String source, final String sink) {
        this.message = message;
        this.source = source;
        this.sink = sink;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public String getSource() {
        return source;
    }

    public void setSource(final String source) {
        this.source = source;
    }

    public String getSink() {
        return sink;
    }

    public void setSink(final String sink) {
        this.sink = sink;
    }
}
