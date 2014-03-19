
package com.demo.launcherdata;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.database.Cursor;

public class LauncherSet {
    private final static Logger logger = LoggerFactory.getLogger(
            LauncherSet.class.getSimpleName());

    ArrayList<ItemModel> items = new ArrayList<ItemModel>();

    private void add(ItemModel model) {
        items.add(model);
    }

    public void copyAllFrom(Cursor c) {
        // TODO Auto-generated method stub
        final int idIndex = c.getColumnIndexOrThrow(Columns._ID);
        final int titleIndex = c.getColumnIndexOrThrow(Columns.TITLE);
        final int intentIndex = c.getColumnIndexOrThrow(Columns.INTENT);
        final int containerIndex = c.getColumnIndexOrThrow(Columns.CONTAINER);
        final int screenIndex = c.getColumnIndexOrThrow(Columns.SCREEN);
        final int cellXIndex = c.getColumnIndexOrThrow(Columns.CELLX);
        final int cellYIndex = c.getColumnIndexOrThrow(Columns.CELLY);
        final int spanXIndex = c.getColumnIndexOrThrow(Columns.SPANX);
        final int spanYIndex = c.getColumnIndexOrThrow(Columns.SPANY);
        final int itemTypeIndex = c.getColumnIndexOrThrow(Columns.ITEM_TYPE);
        final int appWidgetIdIndex = c.getColumnIndexOrThrow(Columns.APPWIDGET_ID);

        final int iconTypeIndex = c.getColumnIndexOrThrow(Columns.ICON_TYPE);
        final int iconPackageIndex = c.getColumnIndexOrThrow(Columns.ICON_PACKAGE);
        final int iconResourceIndex = c.getColumnIndexOrThrow(Columns.ICON_RESOURCE);
        final int uriIndex = c.getColumnIndexOrThrow(Columns.URI);
        final int displayModeIndex = c.getColumnIndexOrThrow(Columns.DISPLAY_MODE);
        final int iconIndex = c.getColumnIndexOrThrow(Columns.ICON);

        while (c.moveToNext()) {
            ItemModel model = new ItemModel();
            model._id = c.getInt(idIndex);
            model.title = c.getString(titleIndex);
            model.intent = c.getString(intentIndex);
            model.container = c.getInt(containerIndex);
            model.screen = c.getInt(screenIndex);
            model.cellX = c.getInt(cellXIndex);
            model.cellY = c.getInt(cellYIndex);
            model.spanX = c.getInt(spanXIndex);
            model.spanY = c.getInt(spanYIndex);
            model.itemType = c.getInt(itemTypeIndex);
            model.appWidgetId = c.getInt(appWidgetIdIndex);

            model.iconType = c.getInt(iconTypeIndex);
            model.iconPackage = c.getString(iconPackageIndex);
            model.iconResource = c.getString(iconResourceIndex);
            model.uri = c.getString(uriIndex);

            model.displayMode = c.getInt(displayModeIndex);
            model.icon = c.getBlob(iconIndex);
            add(model);
        }
    }

    public void print() {

        logger.info(">id>title>intent>container" +
                ">screen>cellX>cellY>spanX>spanY"
                + ">itemType>appWidgetId>icon_size>iconType"
                + ">iconPackage>iconResource>uri>displayMode");
        // isShortcut>
        for (ItemModel item : items) {
            print(item);
        }
    }

    private void print(ItemModel item) {

        logger.info(">" + item._id + ">" + item.title + ">" + getComponent(item.intent) + ">"
                + item.container + ">" + item.screen + ">"
                + item.cellX + ">" + item.cellY + ">" + item.spanX + ">" + item.spanY
                + ">" + item.itemType + ">" + item.appWidgetId + ">" + getLen(item.icon)
                + ">" + item.iconType + ">" + item.iconPackage + ">" + item.iconResource + ">"
                + item.uri + ">" + item.displayMode
                );
    }

    private String getComponent(String intent) {
        return (intent == null) ? "null" : intent.substring(
                10 + intent.indexOf("component="),
                intent.length() - ";end".length());
    }

    private String getLen(byte[] b) {
        return (b == null) ? "0" : "" + b.length;
    }

    public List<ItemModel> getItems() {
        return items;
    }

}
