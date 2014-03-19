
package com.demo.launcherdata;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity implements IEventHandler {
    // private final static Logger logger = LoggerFactory.getLogger(
    // MainActivity.class.getSimpleName());

    private TextView tView;
    /**
     * a containor to show the icons from launcher desktop.
     */
    private GridView grid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tView = (TextView) findViewById(R.id.title);
        grid = (GridView) findViewById(R.id.grid);

        asyncTask(new LauncherItemsTask(this), 1000);

        // asyncTask(new WallpaperTask(this),2000);

    }

    private void asyncTask(TimerTask task, long delay) {
        new Timer().schedule(task, delay);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.obj instanceof LauncherSet) {
                List<ItemModel> items = ((LauncherSet) msg.obj).getItems();
                // got items , show icons in the gridview
                if (null != items && items.size() > 0) {
                    showGrid(items);
                } else {
                    showError();
                }
            }
        };
    };

    @Override
    public void handleMessage(Object obj) {
        Message msg = handler.obtainMessage();
        msg.obj = obj;
        handler.sendMessage(msg);
    }

    /**
     * show error info
     */
    protected void showError() {
        tView.setTextColor(Color.RED);
        tView.setText("error! see logcat info.");
    }

    /**
     * show desktop items in my own view
     * 
     * @param items
     */
    private void showGrid(List<ItemModel> items) {
        // IconAdapter show only icons.
        // to show more info, create another Adapter.
        grid.setAdapter(new IconAdapter(items));
        grid.setVisibility(View.VISIBLE);
    }

    /**
     * show icons in ImageViews
     */
    class IconAdapter extends BaseAdapter {
        List<ItemModel> items;

        public IconAdapter(List<ItemModel> items) {
            this.items = items;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (null == convertView) {
                convertView = new ImageView(MainActivity.this);
                convertView.setLayoutParams(new GridView.LayoutParams(
                        LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
            }
            final byte[] icon = items.get(position).icon;
            if (null != icon) {
                // no cache, just for demo
                Bitmap bitmap = bitmapFromBytes(icon);
                if (null != bitmap) {
                    ((ImageView) convertView).setImageBitmap(bitmap);
                }
            }
            return convertView;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

    }

    /**
     * decode bitmap from byte array
     */
    public static Bitmap bitmapFromBytes(final byte[] bytes) {
        // use default options ,just for demo
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, null);
    }

}
