
package com.example.intenttestcaller;

import java.util.ArrayList;

import android.database.Cursor;
import android.util.Log;

public class LauncherSet {
    ArrayList<ItemModel> items = new ArrayList<ItemModel>();

    private void add(ItemModel model) {
        items.add(model);
    }

    public void copyAllFrom(Cursor c) {
        // TODO Auto-generated method stub
        final int idIndex = c.getColumnIndexOrThrow(ItemInfo._ID);
        final int titleIndex = c.getColumnIndexOrThrow(ItemInfo.TITLE);
        final int intentIndex = c.getColumnIndexOrThrow(ItemInfo.INTENT);
        final int containerIndex = c.getColumnIndexOrThrow(ItemInfo.CONTAINER);
        final int screenIndex = c.getColumnIndexOrThrow(ItemInfo.SCREEN);
        final int cellXIndex = c.getColumnIndexOrThrow(ItemInfo.CELLX);
        final int cellYIndex = c.getColumnIndexOrThrow(ItemInfo.CELLY);
        final int spanXIndex = c.getColumnIndexOrThrow(ItemInfo.SPANX);
        final int spanYIndex = c.getColumnIndexOrThrow(ItemInfo.SPANY);
        final int itemTypeIndex = c.getColumnIndexOrThrow(ItemInfo.ITEM_TYPE);
        final int appWidgetIdIndex = c.getColumnIndexOrThrow(ItemInfo.APPWIDGET_ID);

        final int iconTypeIndex = c.getColumnIndexOrThrow(ItemInfo.ICON_TYPE);
        final int iconPackageIndex = c.getColumnIndexOrThrow(ItemInfo.ICON_PACKAGE);
        final int iconResourceIndex = c.getColumnIndexOrThrow(ItemInfo.ICON_RESOURCE);
        final int uriIndex = c.getColumnIndexOrThrow(ItemInfo.URI);
        final int displayModeIndex = c.getColumnIndexOrThrow(ItemInfo.DISPLAY_MODE);
        final int iconIndex = c.getColumnIndexOrThrow(ItemInfo.ICON);

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

        Log.i("intent", ">id>title>intent>container" +
                ">screen>cellX>cellY>spanX>spanY"
                + ">itemType>appWidgetId>icon_size>iconType"
                + ">iconPackage>iconResource>uri>displayMode");
        // isShortcut>
        for (ItemModel item : items) {
            print(item);
        }
    }

    private void print(ItemModel item) {

        Log.i("intent", ">" + item._id + ">" + item.title + ">" + getComponent(item.intent) + ">"
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

}
