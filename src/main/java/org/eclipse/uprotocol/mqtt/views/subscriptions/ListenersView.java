package org.eclipse.uprotocol.mqtt.views.subscriptions;

import org.eclipse.uprotocol.mqtt.data.ListenerEntry;
import org.eclipse.uprotocol.mqtt.services.UListenerService;
import org.eclipse.uprotocol.mqtt.services.UTransportService;
import org.eclipse.uprotocol.mqtt.util.DeviceType;
import org.eclipse.uprotocol.mqtt.views.MainLayout;
import org.eclipse.uprotocol.v1.UUri;
import org.springframework.data.domain.PageRequest;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import com.vaadin.flow.theme.lumo.LumoUtility;

@PageTitle("Listener Management")
@Route(value = "listeners", layout = MainLayout.class)
@Uses(Icon.class)
public class ListenersView extends Div {

    FormLayout formLayout;
    private final UTransportService uTransportService;
    private final UListenerService uListenerService;

    public ListenersView(UTransportService uTransportService, UListenerService uListenerService) {
        this.uTransportService = uTransportService;
        this.uListenerService = uListenerService;
        setSizeFull();
        addClassNames("listeners-view");

        VerticalLayout layout = new VerticalLayout(createAddGrid(),createDataGrid());
        layout.setSizeFull();
        layout.setPadding(true);
        layout.setSpacing(false);
        add(layout);
    }

    private Component createAddGrid() {
        // Listener Name
        Header listenerHeader = new Header();
        listenerHeader.add("Listener Details");
        listenerHeader.getStyle().setFontSize("2em");

        TextField listenerName = new TextField("Listener Name");
        Header sourceHeader = new Header();

        // Source UUri Details
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

        // Blank lines for prettier formatting
        final Span blankLine = new Span("");
        blankLine.getStyle().setHeight("2em");
        final Span spacingButton = new Span("");

        // Button as logic trigger
        Button addListenerButton = new Button("Add Listener");

        // More space
        final Span blankLineEnd = new Span("");
        blankLineEnd.getStyle().setHeight("2em");

        addListenerButton.addClickListener(buttonClickEvent -> {
            extractInfoAndAddListener(sourceAuthorityName, sourceUeId, sourceUeVersionMajor, sourceResourceId,
                    sinkAuthorityName, sinkUeId, sinkUeVersionMajor, sinkResourceId, listenerName);
        });

        // Format layout
        formLayout = new FormLayout();
        formLayout.setColspan(listenerHeader,8);
        formLayout.setColspan(listenerName,3);
        formLayout.setColspan(sourceHeader,8);
        formLayout.setColspan(sourceAuthorityName,2);
        formLayout.setColspan(sourceUeId,2);
        formLayout.setColspan(sourceUeVersionMajor,2);
        formLayout.setColspan(sourceResourceId,2);
        formLayout.setColspan(sinkHeader,8);
        formLayout.setColspan(sinkAuthorityName,2);
        formLayout.setColspan(sinkUeId,2);
        formLayout.setColspan(sinkUeVersionMajor,2);
        formLayout.setColspan(sinkResourceId,2);
        formLayout.setColspan(blankLine,8);
        formLayout.setColspan(spacingButton,2);
        formLayout.setColspan(addListenerButton,4);
        formLayout.setColspan(blankLineEnd,8);
        formLayout.add(listenerHeader,listenerName,sourceHeader,sourceAuthorityName,sourceUeId
                ,sourceUeVersionMajor,sourceResourceId,sinkHeader,sinkAuthorityName,sinkUeId,sinkUeVersionMajor
                ,sinkResourceId,blankLine,spacingButton,addListenerButton,blankLineEnd);
        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 8)
        );
        return formLayout;
    }

    private void extractInfoAndAddListener(final TextField sourceAuthorityName, final TextField sourceUeId,
            final TextField sourceUeVersionMajor, final TextField sourceResourceId, final TextField sinkAuthorityName,
            final TextField sinkUeId, final TextField sinkUeVersionMajor, final TextField sinkResourceId,
            final TextField listenerName) {
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
        uTransportService.registerListener(listenerName.getValue(), sourceUUri, sinkUUri);
        UI.getCurrent().getPage().reload();
    }

    private Component createDataGrid() {
        // Prepare columns
        Grid<ListenerEntry> grid = new Grid<>(ListenerEntry.class, false);
        grid.addColumn("listenerName").setAutoWidth(true);
        grid.addColumn("source").setAutoWidth(true);
        grid.addColumn("sink").setAutoWidth(true);

        // Define source for list items
        grid.setItems(query -> uListenerService.getAllAsEntryList(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());

        // Vaadin specific stuff
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addClassNames(LumoUtility.Border.TOP, LumoUtility.BorderColor.CONTRAST_10);

        return grid;
    }
}
