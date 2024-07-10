package org.eclipse.uprotocol.mqtt.services;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import com.hivemq.client.mqtt.mqtt5.message.connect.connack.Mqtt5ConnAck;

import org.springframework.stereotype.Service;

import jakarta.validation.constraints.NotNull;

@Service
public class MqttService {

    Mqtt5AsyncClient mqttClient;

    public MqttService() throws ExecutionException, InterruptedException {
        mqttClient = MqttClient.builder()
                .useMqttVersion5()
                .identifier(java.util.UUID.randomUUID().toString())
                .serverHost("mqttbroker")
                .serverPort(1883)
                .simpleAuth()
                    .username("java_client")
                    .password("java_pw".getBytes())
                    .applySimpleAuth()
                .buildAsync();

        final CompletableFuture<@NotNull Mqtt5ConnAck> connect = mqttClient.connect();

        connect.get();
    }

    public Mqtt5AsyncClient getMqttClient() {
        return mqttClient;
    }
}
