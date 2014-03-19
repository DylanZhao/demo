
package com.demo.launcherdata;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;

public class LauncherItemsTask extends TimerTask {
    private final static Logger logger = LoggerFactory.getLogger(
            LauncherItemsTask.class.getSimpleName());

    // 原生android在2.2之前(<8)是"com.android.launcher.settings"。
    // 原生android在2.2开始(>=8)是"com.android.launcher2.settings"。
    private final static String AUTHORITY = "com.android.launcher.settings";
    private final static String AUTHORITY2 = "com.android.launcher2.settings";

    private final static String READ_PERMISSION =
            "com.android.launcher.permission.READ_SETTINGS";
    private final static String WRITE_PERMISSION =
            "com.android.launcher.permission.WRITE_SETTINGS";

    private Context context;

    public LauncherItemsTask(Context context) {
        this.context = context;
    }

    @Override
    public void run() {
        getLauncherItems();
    }

    /**
     * 查询桌面元素信息（位置、尺寸、图元等），暂时输出到日志
     */
    private void getLauncherItems() {
        final List<String> authorities = getAllAuth();
        for (String authority : authorities) {
            if (getFromProvider(authority)) {
                return;
            }
        }
        notify(new LauncherSet());
    }

    /**
     * @return 所有可能的AUTHORITY，优先级：遍历程序匹配PERMISSION > AUTHORITY2 > AUTHORITY
     */
    private List<String> getAllAuth() {
        final List<String> allAuth = getAuthorityFromPermission(context);
        allAuth.add(AUTHORITY2);
        allAuth.add(AUTHORITY);
        return allAuth;
    }

    /**
     * retrieve the right authority by searching for a provider that declares
     * the read/write permissions
     * 
     * @param context
     * @return TODO： do we need a list ? maybe one single is enough .
     */
    private static List<String> getAuthorityFromPermission(final Context context) {
        final ArrayList<String> authorities = new ArrayList<String>();
        final List<PackageInfo> packs = context.getPackageManager()
                .getInstalledPackages(PackageManager.GET_PROVIDERS);
        if (packs != null) {
            for (PackageInfo pack : packs) {
                if (pack.providers != null) {
                    for (ProviderInfo provider : pack.providers) {
                        // TODO Q: is '||' ok enough ? should it be '&&'?
                        if (READ_PERMISSION.equals(provider.readPermission) ||
                                WRITE_PERMISSION.equals(provider.writePermission)) {

                            authorities.add(provider.authority);
                            logger.info("got auth:" + provider.authority
                                    + ",pkg:" + pack.packageName);
                        }
                    }
                }
            }
        }
        return authorities;
    }

    /**
     * get data from Provider
     */
    private boolean getFromProvider(final String authority) {
        if (null == authority || authority.length() == 0) {
            return false;
        }
        logger.info("calling query by authority:" + authority);

        final Uri CONTENT_URI = Uri.parse(
                "content://" + authority + "/favorites?notify=false");
        final String[] columns = ItemModel.getColumns();
        final Cursor c = context.getContentResolver()
                .query(CONTENT_URI, columns, null, null, null);
        if (null == c || !c.moveToFirst()) {
            logger.info("result of query:'" + authority + "' is nothing!");
            return false;
        }
        final LauncherSet set = new LauncherSet();
        set.copyAllFrom(c);
        c.close();
        set.print();
        notify(set);
        return true;

    }

    private void notify(final LauncherSet set) {

        if (context instanceof IEventHandler) {
            ((IEventHandler) context).handleMessage(set);
        }
    }

}
