
package com.demo.launcherdata;

import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.WallpaperInfo;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class WallpaperTask extends TimerTask {
    private final static Logger logger = LoggerFactory.getLogger(
            WallpaperTask.class.getSimpleName());

    private Context context;

    public WallpaperTask(Context context) {
        this.context = context;
    }

    @Override
    public void run() {

        getWallPaper();

        try {
            getWallpaperList();
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void getWallpaperList() throws NameNotFoundException {
        Resources res = context.getPackageManager().
                getResourcesForApplication("com.android.launcher");
        // res.getDrawable();
        res.getDisplayMetrics();
    }

    private void getWallPaper() {
        WallpaperManager wpm = (WallpaperManager) context.getSystemService(
                Context.WALLPAPER_SERVICE);
        WallpaperInfo wpInfo = wpm.getWallpaperInfo();

        // 获取壁纸管理器
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
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
        logger.info("wallpaper size:" + getLen(bm));
    }

    private String getLen(Bitmap bm) {
        return (bm == null) ? "0" : ",w=" + bm.getWidth() +
                ",h=" + bm.getHeight() +
                // note: 3.0开始有方法 getByteCount
                // 通用的方法bm.getRowBytes() * bm.getHeight()
                ",l=" + bm.getRowBytes() * bm.getHeight();
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
