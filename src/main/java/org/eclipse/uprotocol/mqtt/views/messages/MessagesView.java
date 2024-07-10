package org.eclipse.uprotocol.mqtt.views.messages;

import org.eclipse.uprotocol.mqtt.data.Message;
import org.eclipse.uprotocol.mqtt.services.MessageService;
import org.eclipse.uprotocol.mqtt.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import com.vaadin.flow.theme.lumo.LumoUtility;

import org.springframework.data.domain.PageRequest;

@PageTitle("Received Messages")
@Route(value = "messages", layout = MainLayout.class)
@Uses(Icon.class)
public class MessagesView extends Div {

    private final MessageService messageService;

    public MessagesView(MessageService messageService) {
        this.messageService = messageService;
        setSizeFull();
        addClassNames("messages-view");

        VerticalLayout layout = new VerticalLayout(createGrid());
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        add(layout);
    }

    private Component createGrid() {
        Grid<Message> grid = new Grid<>(Message.class, false);
        grid.addColumn("message").setAutoWidth(true);
        grid.addColumn("source").setAutoWidth(true);
        grid.addColumn("sink").setAutoWidth(true);

        grid.setItems(query -> messageService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query))).stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addClassNames(LumoUtility.Border.TOP, LumoUtility.BorderColor.CONTRAST_10);

        return grid;
    }
}
