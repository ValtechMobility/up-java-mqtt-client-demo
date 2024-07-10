package org.eclipse.uprotocol.mqtt.services;

import java.nio.charset.Charset;
import java.util.concurrent.ExecutionException;

import com.google.protobuf.ByteString;

import org.eclipse.uprotocol.mqtt.TransportFactory;
import org.eclipse.uprotocol.mqtt.data.ListenerEntry;
import org.eclipse.uprotocol.mqtt.data.MessageRepository;
import org.eclipse.uprotocol.transport.UListener;
import org.eclipse.uprotocol.transport.UTransport;
import org.eclipse.uprotocol.v1.UAttributes;
import org.eclipse.uprotocol.v1.UMessage;
import org.eclipse.uprotocol.v1.UUID;
import org.eclipse.uprotocol.v1.UUri;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;

@DataJpaTest
class uProtocolClientServiceTest {

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

        final UUri client = UUri.newBuilder()
            .setAuthorityName("mcu1.example.com")
            .setUeId(12345678)
            .setUeVersionMajor(2)
            .setResourceId(3).build();

        final UListener myListener = transportService.registerListener("myListener", client, UUri.newBuilder()
                .setAuthorityName("testSource.someUri.network")
                .setUeId(0xffff)
                .setUeVersionMajor(0xff)
                .setResourceId(0xffff)
                .build());

        final UMessage uMessage = UMessage.newBuilder()
                .setAttributes(
                        UAttributes.newBuilder()
                                .setId(UUID.newBuilder().build())
                                .setTtl(1000)
                                .setReqid(UUID.newBuilder().build())
                                .setToken("SomeToken")
                                .setTraceparent("someTraceParent")
                                .setSource(UUri.newBuilder()
                                        .setAuthorityName("testSource.someUri.network")
                                        .build())
                                .setSink(UUri.newBuilder()
                                        .setAuthorityName("testDestination.someUri.network")
                                        .build())
                                .build())
                .setPayload(ByteString.copyFrom("This is my glorious message: Hey", Charset.defaultCharset()))
                .build();

        final UUri.Builder builder = UUri.newBuilder();
        builder.setAuthorityName("mcu1.example.com");
        builder.setUeId(12345678);
        builder.setUeVersionMajor(2);
        builder.setResourceId(3);
        final UUri builtClientUUri = builder.build();

        final UTransport myTransport = transportService.getTransport(builtClientUUri);

        System.out.println(uListenerService.getAllAsEntryList(PageRequest.ofSize(1000)).stream().map(ListenerEntry::getListenerName).toList());

        myTransport.send(uMessage);

        Thread.sleep(1000);

        messageRepository.findAll().forEach(message -> System.out.println(message.getMessage()));
    }
}