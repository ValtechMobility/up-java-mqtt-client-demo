package org.eclipse.uprotocol.mqtt.listener;

import java.io.Serializable;
import java.nio.charset.Charset;

import com.vaadin.flow.component.notification.Notification;

import org.eclipse.uprotocol.mqtt.data.Message;
import org.eclipse.uprotocol.mqtt.data.MessageRepository;
import org.eclipse.uprotocol.transport.UListener;
import org.eclipse.uprotocol.v1.UMessage;



public class AsyncUPListener implements UListener, Serializable {

    MessageRepository messageRepository;

    public AsyncUPListener() {
    }

    public AsyncUPListener(final MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public void onReceive(final UMessage uMessage) {
        final Message message =
                new Message(uMessage.getPayload().toString(Charset.defaultCharset()),
                        uMessage.getAttributes().getSource().toString(),
                        uMessage.getAttributes().getSink().toString());
        messageRepository.save(message);
    }
}
