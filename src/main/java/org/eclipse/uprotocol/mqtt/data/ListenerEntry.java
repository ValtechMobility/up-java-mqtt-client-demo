package org.eclipse.uprotocol.mqtt.data;

public class ListenerEntry {
    private final String listenerName;
    private final String source;
    private final String sink;

    public ListenerEntry(final String listenerName, final String source, final String sink) {
        this.listenerName = listenerName;
        this.source = source;
        this.sink = sink;
    }

    public String getListenerName() {
        return listenerName;
    }

    public String getSource() {
        return source;
    }

    public String getSink() {
        return sink;
    }
}
