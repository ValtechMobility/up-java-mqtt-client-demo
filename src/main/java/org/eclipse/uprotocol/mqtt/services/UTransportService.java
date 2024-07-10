package org.eclipse.uprotocol.mqtt.services;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.uprotocol.mqtt.TransportFactory;
import org.eclipse.uprotocol.transport.UListener;
import org.eclipse.uprotocol.transport.UTransport;
import org.eclipse.uprotocol.v1.UMessage;
import org.eclipse.uprotocol.v1.UUri;
import org.springframework.stereotype.Service;

@Service
public class UTransportService {

    private final MqttService mqttService;
    private final UListenerService uListenerService;
    private final Map<UUri,UTransport> inMemoryTransports = new HashMap<>();

    public UTransportService(MqttService mqttService, UListenerService uListenerService) {
        this.mqttService = mqttService;
        this.uListenerService = uListenerService;
    }

    public UTransport getTransport(UUri source){
        if(!inMemoryTransports.containsKey(source)){
            inMemoryTransports.put(source,TransportFactory.createInstance(source, mqttService.getMqttClient()));
        }
        return inMemoryTransports.get(source);
    }

    public UListener registerListener(String listenerName, UUri sourceUUri ,UUri sinkUUri) {
        final UListener uListener = uListenerService.saveListener(listenerName, sourceUUri, sinkUUri);
        final UTransport transport = getTransport(sourceUUri);
        transport.registerListener(sinkUUri,uListener);
        return uListener;
    }

    public void unregisterListener(String listenerName) {
        uListenerService.removeByListenerName(listenerName);
    }

    public void send(UMessage uMessage){
        final UTransport transport = getTransport(uMessage.getAttributes().getSource());
        transport.send(uMessage);
    }
}
