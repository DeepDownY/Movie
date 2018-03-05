package com.example.y1247.movie.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.y1247.movie.R;
import com.example.y1247.movie.data.Video;

/**
 * Created by y1247 on 2017/5/5.
 */

public class VideoView extends LinearLayout {

    Context context;
    TextView tv_VideoName;
    ImageView iv_show;
    Video video;

    public VideoView(Context context) {
        this(context,null);
    }

    public VideoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.video_layout,this);
        iv_show = (ImageView) findViewById(R.id.iv_show);
        tv_VideoName = (TextView) findViewById(R.id.tv_videoName);
    }

    public void setVideo(Video video) {
        this.video = video;
        tv_VideoName.setText(video.getName());
    }

    public void setShowClicked(OnClickListener listen){
        iv_show.setOnClickListener(listen);
    }
}
