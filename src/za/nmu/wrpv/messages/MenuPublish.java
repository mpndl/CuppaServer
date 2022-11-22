package za.nmu.wrpv.messages;

import za.nmu.wrpv.*;

import java.io.*;
import java.util.HashMap;
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
        String language = params.get("language").toString();
        String code = params.get("code").toString();

        String fileName = "menu.xml";

        if (params != null && params.containsKey("language"))
            if (XMLHandler.fileExists("menu_"+language+".xml"))
                fileName = "menu_"+language+".xml";

        MenuItems.clear();
        XMLHandler.loadMenuFromXML(fileName, getRate("xrates.csv", code));
        Map<String, Object> params = new HashMap<>();
        System.out.println(MenuItems.getItems().size());
        params.put(key, MenuItems.getItems());
        Message<ClientHandler> message = new MenuPublish(null, topic, params);
        handler.publish(message);
    }

    public Rate getRate(String file, String code)
    {
        try {
            Rate rate = new Rate();
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line;
            while ((line = br.readLine()) != null) {
                String[] cells = line.split(",");
                rate.description = cells[0];
                rate.code = cells[1];
                rate.oneCur = Double.parseDouble(cells[2].substring(0, 5));
                rate.inCur = Double.parseDouble(cells[3].substring(0, 5));

                if (rate.code.equals(code))
                    return rate;

            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
