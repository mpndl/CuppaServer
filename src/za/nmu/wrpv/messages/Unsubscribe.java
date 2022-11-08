package za.nmu.wrpv.messages;

import za.nmu.wrpv.ClientHandler;
import za.nmu.wrpv.PubSubBroker;
import za.nmu.wrpv.Subscriber;

import java.io.Serial;

public class Unsubscribe extends Message<ClientHandler> {
    @Serial
    private static final long serialVersionUID = 3L;

    private final String topic;
    private final Subscriber subscriber;

    public Unsubscribe(String topic, Subscriber subscriber) {
        this.topic = topic;
        this.subscriber = subscriber;
    }

    @Override
    public void apply(ClientHandler handler) {
        if (topic != null)
            PubSubBroker.unsubscribe(topic+handler.getClientID(), subscriber);
        else PubSubBroker.unsubscribe(subscriber);
        System.out.println(">>> UNSUBSCRIBED -> " + topic);
    }
}
