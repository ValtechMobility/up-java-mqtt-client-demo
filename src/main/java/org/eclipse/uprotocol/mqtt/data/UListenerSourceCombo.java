package org.eclipse.uprotocol.mqtt.data;

import org.eclipse.uprotocol.mqtt.listener.AsyncUPListener;
import org.eclipse.uprotocol.v1.UUri;

public class UListenerSourceCombo extends AbstractEntity {

    private AsyncUPListener listener;
    private UUri source;
    private UUri sink;

    public UListenerSourceCombo() {
    }

    public UListenerSourceCombo(final UUri source, final UUri sink, final AsyncUPListener listener) {
        this.source = source;
        this.sink = sink;
        this.listener = listener;
    }

    public AsyncUPListener getListener() {
        return listener;
    }

    public UUri getSource() {
        return source;
    }

    public UUri getSink() {
        return sink;
    }
}
