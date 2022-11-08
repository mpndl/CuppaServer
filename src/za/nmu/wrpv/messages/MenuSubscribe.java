package za.nmu.wrpv.messages;

import za.nmu.wrpv.ClientHandler;
import za.nmu.wrpv.MenuItems;
import za.nmu.wrpv.Subscriber;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class MenuSubscribe extends Subscribe implements Serializable {
    @Serial
    private final static long serialVersionUID = 21L;
    public static final String key = "menu";

    public MenuSubscribe(String topic, Subscriber subscriber) {
        super(topic, subscriber);
    }

    @Override
    public void apply(ClientHandler handler) {
        Map<String, Object> params = new HashMap<>();
        params.put(key, MenuItems.getItems());
        Message<ClientHandler> message = new MenuPublish(null, topic, params);
        handler.publish(message);
    }

    @Override
    public String toString() {
        return key;
    }
}
