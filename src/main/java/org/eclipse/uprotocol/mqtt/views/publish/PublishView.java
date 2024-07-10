package org.eclipse.uprotocol.mqtt.views.publish;

import org.eclipse.uprotocol.mqtt.services.UTransportService;
import org.eclipse.uprotocol.mqtt.views.MainLayout;
import org.eclipse.uprotocol.v1.UAttributes;
import org.eclipse.uprotocol.v1.UMessage;
import org.eclipse.uprotocol.v1.UUri;

import com.google.protobuf.ByteString;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

@PageTitle("Send Messages")
@Route(value = "", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class PublishView extends HorizontalLayout {

    UTransportService uTransportService;
    FormLayout formLayout;

    public PublishView(UTransportService uTransportService) {
        this.uTransportService = uTransportService;

        // Create Components

        // Section for Message Payload
        Header messageHeader = new Header();
        messageHeader.setText("Message Details");
        messageHeader.getStyle().setFontSize("2em");
        final TextField messagePayloadTextfield = new TextField("Message Payload");

        // Source UUri Details
        Header sourceHeader = new Header();
        sourceHeader.add("Source Details");
        sourceHeader.getStyle().setFontSize("2em");

        TextField sourceAuthorityName = new TextField("Authority Name");

        TextField sourceUeId = new TextField("uEntity ID");
        sourceUeId.setValue("0");

        TextField sourceUeVersionMajor = new TextField("uEntity Version Major");
        sourceUeVersionMajor.setValue("0");

        TextField sourceResourceId = new TextField("Resource ID");
        sourceResourceId.setValue("0");

        // Sink UUri Details
        Header sinkHeader = new Header();
        sinkHeader.add("Sink Details");
        sinkHeader.getStyle().setFontSize("2em");

        TextField sinkAuthorityName = new TextField("Authority Name");

        TextField sinkUeId = new TextField("uEntity ID");
        sinkUeId.setValue("0xFFFF");

        TextField sinkUeVersionMajor = new TextField("uEntity Version Major");
        sinkUeVersionMajor.setValue("0xFF");

        TextField sinkResourceId = new TextField("Resource ID");
        sinkResourceId.setValue("0xFFFF");

        // Insert blank lines for prettier formatting
        final Span blankLine = new Span("");
        blankLine.getStyle().setHeight("2em");
        final Span spacingButton = new Span("");

        // Button with trigger for uTransport
        Button sendMessageButton = new Button("Send message");
        sendMessageButton.addClickListener(e -> {
            extractInfoAndSendMessage(uTransportService, sourceAuthorityName, sourceUeId, sourceUeVersionMajor,
                    sourceResourceId, sinkAuthorityName, sinkUeId, sinkUeVersionMajor, sinkResourceId,
                    messagePayloadTextfield);
        });
        sendMessageButton.addClickShortcut(Key.ENTER);

        // Formatting
        formLayout = new FormLayout();
        formLayout.setColspan(messageHeader,8);
        formLayout.setColspan(messagePayloadTextfield, 8);
        formLayout.setColspan(sourceHeader, 8);
        formLayout.setColspan(sourceAuthorityName, 2);
        formLayout.setColspan(sourceUeId, 2);
        formLayout.setColspan(sourceUeVersionMajor, 2);
        formLayout.setColspan(sourceResourceId, 2);
        formLayout.setColspan(sinkHeader, 8);
        formLayout.setColspan(sinkAuthorityName, 2);
        formLayout.setColspan(sinkUeId, 2);
        formLayout.setColspan(sinkUeVersionMajor, 2);
        formLayout.setColspan(sinkResourceId, 2);
        formLayout.setColspan(blankLine, 8);
        formLayout.setColspan(spacingButton, 2);
        formLayout.setColspan(sendMessageButton, 4);

        // Add components to layout
        formLayout.add(messageHeader, messagePayloadTextfield, sourceHeader, sourceAuthorityName, sourceUeId
                , sourceUeVersionMajor, sourceResourceId, sinkHeader, sinkAuthorityName, sinkUeId, sinkUeVersionMajor
                , sinkResourceId, blankLine,spacingButton, sendMessageButton);
        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 8)
        );

        setMargin(true);
        add(formLayout);
    }

    private static void extractInfoAndSendMessage(final UTransportService uTransportService, final TextField sourceAuthorityName,
            final TextField sourceUeId, final TextField sourceUeVersionMajor, final TextField sourceResourceId,
            final TextField sinkAuthorityName, final TextField sinkUeId, final TextField sinkUeVersionMajor,
            final TextField sinkResourceId, final TextField messagePayloadTextfield) {
        final UUri sourceUUri =
                UUri.newBuilder()
                        .setAuthorityName(sourceAuthorityName.getValue())
                        .setUeId(Integer.decode(sourceUeId.getValue()))
                        .setUeVersionMajor(Integer.decode(sourceUeVersionMajor.getValue()))
                        .setResourceId(Integer.decode(sourceResourceId.getValue()))
                        .build();
        final UUri sinkUUri =
                UUri.newBuilder()
                        .setAuthorityName(sinkAuthorityName.getValue())
                        .setUeId(Integer.decode(sinkUeId.getValue()))
                        .setUeVersionMajor(Integer.decode(sinkUeVersionMajor.getValue()))
                        .setResourceId(Integer.decode(sinkResourceId.getValue()))
                        .build();
        final UMessage uMessage = UMessage.newBuilder()
                .setPayload(ByteString.copyFromUtf8(messagePayloadTextfield.getValue()))
                .setAttributes(UAttributes.newBuilder()
                        .setSource(sourceUUri)
                        .setSink(sinkUUri)
                        .build())
                .build();
        uTransportService.send(uMessage);
        Notification.show("Message sent!");
    }
}
