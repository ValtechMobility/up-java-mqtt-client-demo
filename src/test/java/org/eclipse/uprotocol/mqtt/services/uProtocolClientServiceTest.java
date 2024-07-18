package org.eclipse.uprotocol.mqtt.services;

import java.nio.charset.Charset;
import java.util.concurrent.ExecutionException;

import com.google.protobuf.ByteString;

import org.eclipse.uprotocol.communication.UPayload;
import org.eclipse.uprotocol.mqtt.data.MessageRepository;
import org.eclipse.uprotocol.transport.UListener;
import org.eclipse.uprotocol.transport.UTransport;
import org.eclipse.uprotocol.transport.builder.UMessageBuilder;
import org.eclipse.uprotocol.v1.UMessage;
import org.eclipse.uprotocol.v1.UPayloadFormat;
import org.eclipse.uprotocol.v1.UUri;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class UProtocolClientServiceTest {

    @Autowired
    MessageRepository messageRepository;

    MqttService mqttService;
    UListenerService uListenerService;
    UTransportService transportService;

    @BeforeEach
    void setUp() throws ExecutionException, InterruptedException {
        mqttService = new MqttService();
        uListenerService = new UListenerService(messageRepository);
        transportService = new UTransportService(mqttService, uListenerService);
    }

    @Disabled("MQTT Broker needs to run and accept connections from the client for this test")
    @Test
    void tbd() throws InterruptedException, ExecutionException {

        final UUri source = UUri.newBuilder()
            .setAuthorityName("mcu1.example.com")
            .setUeId(12345678)
            .setUeVersionMajor(2)
            .setResourceId(3).build();

        final UUri someSink = UUri.newBuilder()
                .setAuthorityName("testSource.someUri.network")
                .setUeId(0xffff)
                .setUeVersionMajor(0xff)
                .setResourceId(0xffff)
                .build();

        final UListener myListener = transportService.registerListener("myListener", source, someSink);

        final UMessage uMessage = UMessageBuilder.notification(source,someSink).build(new UPayload(
                                ByteString.copyFrom("This is my glorious message: Hey", Charset.defaultCharset()),
                                UPayloadFormat.UPAYLOAD_FORMAT_RAW));

        final UTransport myTransport = transportService.getTransport(source);

        myTransport.send(uMessage);

        //Ignore this section here :P
        Thread.sleep(1000);

        messageRepository.findAll().forEach(message -> System.out.println(message.getMessage()));
    }
}