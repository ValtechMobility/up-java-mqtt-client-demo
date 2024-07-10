package org.eclipse.uprotocol.mqtt.services;

import org.eclipse.uprotocol.mqtt.data.Message;
import org.eclipse.uprotocol.mqtt.data.MessageRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    private final MessageRepository repository;

    public MessageService(MessageRepository repository) {
        this.repository = repository;
    }

    public void persist(Message entity) {
        repository.save(entity);
    }

    public Page<Message> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
