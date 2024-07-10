package org.eclipse.uprotocol.mqtt.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.uprotocol.mqtt.data.ListenerEntry;
import org.eclipse.uprotocol.mqtt.data.MessageRepository;
import org.eclipse.uprotocol.mqtt.data.UListenerSourceCombo;
import org.eclipse.uprotocol.mqtt.listener.AsyncUPListener;
import org.eclipse.uprotocol.transport.UListener;
import org.eclipse.uprotocol.v1.UUri;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class UListenerService {

    MessageRepository messageRepository;
    Map<String,UListenerSourceCombo> inMemoryListeners = new HashMap<>();

    public UListenerService(final MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    private AsyncUPListener createAsyncUPListener() {
        return new AsyncUPListener(messageRepository);
    }

    public UListener saveListener(String listenerName, UUri source, UUri sink) {
        final AsyncUPListener asyncUPListener = createAsyncUPListener();
        final UListenerSourceCombo uListenerSourceCombo = new UListenerSourceCombo(source, sink, asyncUPListener);
        inMemoryListeners.put(listenerName,uListenerSourceCombo);
        return asyncUPListener;
    }

    public void removeByListenerName(final String listenerName) {
        inMemoryListeners.remove(listenerName);
    }

    public List<ListenerEntry> getAllAsEntryList(PageRequest pageRequest) {
        return Map.copyOf(inMemoryListeners).entrySet().stream().limit(pageRequest.getPageSize()).map(mapEntry -> new ListenerEntry(mapEntry.getKey(),
                mapEntry.getValue().getSource().getAuthorityName(),mapEntry.getValue().getSink().getAuthorityName())).toList();
    }
}
