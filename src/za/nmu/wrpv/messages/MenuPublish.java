package za.nmu.wrpv.messages;

import za.nmu.wrpv.ClientHandler;
import za.nmu.wrpv.Item;
import za.nmu.wrpv.Rate;
import static za.nmu.wrpv.XMLHandler.loadMenuFromXML;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static za.nmu.wrpv.Helpers.fileExists;
import static za.nmu.wrpv.Rate.getRate;

public class MenuPublish extends Publish implements Serializable {
    @Serial
    private final static long serialVersionUID = 32L;
    public static final String key = "menu";
    public MenuPublish(Object publisher, String topic, Map<String, Object> params) {
        super(publisher, topic, params);
    }

    @Override
    public void apply(ClientHandler handler) {
        String language = params.getOrDefault("language", "").toString();
        String code = params.get("code").toString();

        String fileName = "menu.xml";

        if (fileExists("menu_"+language+".xml")) fileName = "menu_"+language+".xml";

        Rate rate = getRate("xrates.csv", code);

        List<Item> items = loadMenuFromXML(fileName, rate);

        Map<String, Object> map = new HashMap<>();
        map.put(key, items);

        Message<ClientHandler> message = new MenuPublish(null, topic, map);
        handler.publish(message);

        System.out.println("MENU(" + fileName + ", " + rate.description + ") -> language = " + language + ", currency code = " + code);
    }


}
