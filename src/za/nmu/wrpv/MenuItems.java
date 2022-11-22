package za.nmu.wrpv;

import java.util.ArrayList;
import java.util.List;

public class MenuItems {
    private static final List<Item> items = new ArrayList<>();
    public static void add(Item item) {
        items.add(item);
    }
    public static List<Item> getItems() {
        return items;
    }
    public static void clear() {items.clear();}
}
