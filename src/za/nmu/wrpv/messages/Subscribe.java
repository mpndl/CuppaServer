package za.nmu.wrpv.messages;

import za.nmu.wrpv.ClientHandler;
import za.nmu.wrpv.PubSubBroker;
import za.nmu.wrpv.Subscriber;

import java.io.Serial;

public class Subscribe extends Message<ClientHandler> {
    @Serial
    private static final long serialVersionUID = 2L;

    public String topic;
    public final Subscriber subscriber;

    public Subscribe(String topic, Subscriber subscriber) {
        this.topic = topic;
        this.subscriber = subscriber;
    }

    @Override
    public void apply(ClientHandler handler) {
        Subscriber subscriber = (publisher, topic1, params) -> {
            Message<ClientHandler> message = new Publish(publisher, topic1, params);
            handler.publish(message);
        };
        System.out.println(">>> SUBSCRIBED -> " + topic);
        PubSubBroker.subscribe(topic+handler.getClientID(), subscriber);
    }
}
