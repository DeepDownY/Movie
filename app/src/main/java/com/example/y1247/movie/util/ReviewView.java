package com.example.y1247.movie.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.y1247.movie.R;
import com.example.y1247.movie.data.Review;

/**
 * Created by y1247 on 2017/5/5.
 */

public class ReviewView extends LinearLayout {

    TextView tv_content;
    TextView tv_author;

    public ReviewView(Context context) {
        this(context,null);
    }

    public ReviewView(Context context, AttributeSet attrs) {
        super(context,attrs);
        LayoutInflater.from(context).inflate(R.layout.review_layout, this);
        tv_author = (TextView) findViewById(R.id.tv_author);
        tv_content = (TextView) findViewById(R.id.tv_content);
    }

    public void setReview(Review review){
        tv_content.setText(review.getContent());
        tv_author.setText(review.getAuthor());
    }


}
