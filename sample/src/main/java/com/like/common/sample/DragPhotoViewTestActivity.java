package com.like.common.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.like.common.view.dragphotoview.DragPhotoViewActivity;
import com.like.common.view.dragphotoview.DragPhotoViewInfo;

/**
 * Created by like on 2017/9/22.
 */

public class DragPhotoViewTestActivity extends AppCompatActivity {
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dragphotoview);
        mImageView = (ImageView) findViewById(R.id.imageView);
    }

    public void onClick(View view) {
        startPhotoActivity(this, (ImageView) view);
    }


    public void startPhotoActivity(Context context, ImageView imageView) {
        Intent intent = new Intent(context, DragPhotoViewActivity.class);
        int location[] = new int[2];
        /**
         *iew.getLocationInWindow(location); //获取在当前窗口内的绝对坐标
         *dragPhotoView.getLocationOnScreen(location);//获取在整个屏幕内的绝对坐标
         *getLocationOnScreen计算该视图在全局坐标系中的x，y值，
         *(注意这个值是要从屏幕顶端算起，也就是索包括了通知栏的高度)//获取在当前屏幕内的绝对坐标
         *location [0]--->x坐标,location [1]--->y坐标
         *
         */
        imageView.getLocationOnScreen(location);
        intent.putExtra(DragPhotoViewActivity.KEY_DATA, new DragPhotoViewInfo(location[0], location[1], imageView.getWidth(), imageView.getHeight()));
        context.startActivity(intent);
        overridePendingTransition(0, 0);
    }
}
