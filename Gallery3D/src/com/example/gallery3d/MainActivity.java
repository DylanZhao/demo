package com.example.gallery3d;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
        initView((ImageGroup3) findViewById(R.id.image_group));
    }

    private int[] resIDs = new int[] {
            R.drawable.demo1,
            R.drawable.demo2,
            R.drawable.demo3,
    };

    private void initView(ImageGroup3 imageGroup) {
        imageGroup.setAdapter(new ImageAdapter() {

            @Override
            public void fillImage(ImageView imageView, int position) {
                imageView.setImageResource(resIDs[getResIdx(position)]);
            }
        });
    }

    protected int getResIdx(int position) {
        while (position < 0) {
            position += resIDs.length;
        }
        return position % resIDs.length;
    }
}
