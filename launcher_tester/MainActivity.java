
package com.example.intenttestcaller;

import android.app.Activity;
import android.app.WallpaperInfo;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // doIt();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void doIt() {
        // 这个AUTHORITY字符串有变化，不同系统可能不一样。
        // 原生android在2.2之前(<8)是"com.android.launcher.settings"。
        final String AUTHORITY = "com.android.launcher2.settings";
        final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/favorites?notify=true");

        String[] columns = ItemModel.getColumns();
        Log.i("intent", "calling query by URI:" + CONTENT_URI);
        Cursor c = getContentResolver().query(CONTENT_URI, columns, null, null, null);
        LauncherSet set = new LauncherSet();
        set.copyAllFrom(c);
        c.close();
        set.print();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // Log.i("intent", "caller calling startActivity by action");
        // Intent intent = new Intent("Intent.Test.Call.StartActivity");
        // startActivity(intent);
        
        // getWallPaper();
        
        try {
            getWallpaperList();
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void getWallpaperList() throws NameNotFoundException {
        Resources res = getPackageManager().
                getResourcesForApplication("com.android.launcher");
        res.getDrawable(id);
    }

    private void getWallPaper() {
        WallpaperManager wpm = (WallpaperManager) getSystemService(
                Context.WALLPAPER_SERVICE);
        WallpaperInfo wpInfo = wpm.getWallpaperInfo();

        // 获取壁纸管理器
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        // 获取当前壁纸
        Drawable wallpaperDrawable = wallpaperManager.getDrawable();
        // 将Drawable,转成Bitmap
        Bitmap bm = ((BitmapDrawable) wallpaperDrawable).getBitmap();
        showBitmap(
        // cropBitmap(
        bm
        // )
        );

    }

    private void showBitmap(Bitmap bm) {
        // TODO Auto-generated method stub
        Log.i("wallpaper", "wallpaper size:" + getLen(bm));
    }

    private String getLen(Bitmap bm) {
        return (bm == null) ? "0" : ",w=" + bm.getWidth() +
                ",h=" + bm.getHeight() +
                ",l=" + bm.getByteCount();
    }

    private Bitmap cropBitmap(Bitmap bm) {
        // 需要详细说明一下，mScreenCount、getCurrentWorkspaceScreen()、mScreenWidth、mScreenHeight分别
        // 对应于Launcher中的桌面屏幕总数、当前屏幕下标、屏幕宽度、屏幕高度.等下拿Demo的哥们稍微要注意一下

        // 计算出屏幕的偏移量,截取相应屏幕的Bitmap

        // float step = (bm.getWidth() -
        // Launcher.LauncherPreferenceModel.mScreenWidth)
        // / (LauncherPreferenceModel.mScreenCount - 1);
        //
        // Bitmap pbm = Bitmap.createBitmap(bm, (int) (mLauncher
        // .getCurrentWorkspaceScreen() * step), 0,
        // (int) (LauncherPreferenceModel.mScreenWidth),
        // (int) (LauncherPreferenceModel.mScreenHeight));
        // // 设置 背景
        // layout.setBackgroundDrawable(new BitmapDrawable(pbm));
        return null;
    }

}
