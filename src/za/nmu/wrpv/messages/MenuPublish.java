package za.nmu.wrpv.messages;

import za.nmu.wrpv.ClientHandler;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

public class MenuPublish extends Publish implements Serializable {
    @Serial
    private final static long serialVersionUID = 32L;
    public static final String key = "menu";
    public MenuPublish(Object publisher, String topic, Map<String, Object> params) {
        super(publisher, topic, params);
    }

    @Override
    public void apply(ClientHandler handler) {
        super.apply(handler);
    }
}
