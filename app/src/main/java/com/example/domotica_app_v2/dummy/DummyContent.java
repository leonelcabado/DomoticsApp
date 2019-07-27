package com.example.domotica_app_v2.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {



    /**
     * An array of sample (dummy) items.
     */
    public static final List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();


    /*static {

        // Add some sample items.
        {
            addItem(new DummyItem("1","Automotor El Dorado","1"));
            addItem(new DummyItem("2","Ferreteria La Florida","2"));
            addItem(new DummyItem("3","Iglesia Belen","3"));
            addItem(new DummyItem("4","Pizzeria Olivia","4"));
        }

    }*/

    public static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(String.valueOf(item.id), item);
    }

    /*private static DummyItem createDummyItem(int position) {
        return new DummyItem(String.valueOf(position), "Item " + position, makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }*/

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {

        public final int id;
        public final String locacion;
        public final String lat;
        public final String lon;
        public final int userID;




        public DummyItem(int id, String locacion, String lat,String lon, int userID) {
            this.id = id;
            this.locacion = locacion;
            this.lat = lat;
            this.lon = lon;
            this.userID = userID;
        }
        public String toString() {

            return id+""+locacion;
        }
    }

}
