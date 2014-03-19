/**
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.intenttestcaller;

public class ItemInfo {
    static final String _ID = "_id";
    static final String TITLE = "title";
    static final String INTENT = "intent";
    static final String ITEM_TYPE = "itemType";
    static final int ITEM_TYPE_APPLICATION = 0;
    static final int ITEM_TYPE_SHORTCUT = 1;

    static final String ICON_TYPE = "iconType";
    static final int ICON_TYPE_RESOURCE = 0;
    static final int ICON_TYPE_BITMAP = 1;

    static final String ICON_PACKAGE = "iconPackage";
    static final String ICON_RESOURCE = "iconResource";
    static final String ICON = "icon";

    static final String CONTAINER = "container";
    static final int CONTAINER_DESKTOP = -100;

    static final String SCREEN = "screen";
    static final String CELLX = "cellX";
    static final String CELLY = "cellY";
    static final String SPANX = "spanX";
    static final String SPANY = "spanY";

    static final int ITEM_TYPE_USER_FOLDER = 2;
    static final int ITEM_TYPE_LIVE_FOLDER = 3;
    static final int ITEM_TYPE_APPWIDGET = 4;
    static final int ITEM_TYPE_WIDGET_CLOCK = 1000;
    static final int ITEM_TYPE_WIDGET_SEARCH = 1001;
    static final int ITEM_TYPE_WIDGET_PHOTO_FRAME = 1002;

    static final String APPWIDGET_ID = "appWidgetId";

    @Deprecated
    static final String IS_SHORTCUT = "isShortcut";

    static final String URI = "uri";
    static final String DISPLAY_MODE = "displayMode";

}
