package za.nmu.wrpv.messages;

import za.nmu.wrpv.ClientHandler;
import za.nmu.wrpv.PubSubBroker;

import java.io.Serial;
import java.util.Map;

public class Publish extends Message<ClientHandler>{
    @Serial
    private static final long serialVersionUID = 1L;

    public Object publisher;
    public String topic;
    public Map<String,Object> params;

    public Publish(Object publisher, String topic, Map<String, Object> params) {
        this.publisher = publisher;
        this.topic = topic;
        this.params = params;
    }

    @Override
    public void apply(ClientHandler handler) {
        PubSubBroker.publish(publisher, topic+handler.getClientID(), params);
        System.out.println(">>> PUBLISHED -> " + params.get(key));
    }
}
