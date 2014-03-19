
package com.demo.launcherdata;

import android.database.Cursor;

public class ItemModel {
    int _id;
    String title;
    String intent;
    int container;
    int screen;
    int cellX;
    int cellY;
    int spanX;
    int spanY;
    int itemType;
    int appWidgetId;
    int iconType;
    String iconPackage;
    String iconResource;
    String uri;
    int displayMode;
    byte[] icon;

    // int isShortcut;

    public static String[] getColumns() {
        return new String[] {
                "_id",
                "title",
                "intent",
                "container",
                "screen",
                "cellX",
                "cellY",
                "spanX",
                "spanY",
                "itemType",
                "appWidgetId",

                "iconType",
                "iconPackage",
                "iconResource",
                "uri",
                "displayMode",
                "icon",
        // "isShortcut",
        };
    }

    @Deprecated
    public ItemModel copyFromDB(Cursor c) {
        _id = c.getInt(0);
        title = c.getString(1);
        intent = c.getString(2);
        container = c.getInt(3);
        screen = c.getInt(4);
        cellX = c.getInt(5);
        cellY = c.getInt(6);
        spanX = c.getInt(7);
        spanY = c.getInt(8);
        itemType = c.getInt(9);
        appWidgetId = c.getInt(10);

        iconType = c.getInt(11);
        iconPackage = c.getString(12);
        iconResource = c.getString(13);
        uri = c.getString(14);

        displayMode = c.getInt(15);
        icon = c.getBlob(16);
        return this;
    }
}
