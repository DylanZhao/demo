
package com.demo.launcherdata;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class MainActivity extends Activity {
//    private final static Logger logger = LoggerFactory.getLogger(
//            MainActivity.class.getSimpleName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        asyncTask(new LauncherItemsTask(this), 1000);
        // asyncTask(new WallpaperTask(this),2000);

    }

    // 后台操作放在异步线程中，暂时用系统的定时任务。
    private void asyncTask(TimerTask task, long delay) {
        new Timer().schedule(task, delay);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // logger.info( "caller calling startActivity by action");
        // Intent intent = new Intent("Intent.Test.Call.StartActivity");
        // startActivity(intent);

    }

}
