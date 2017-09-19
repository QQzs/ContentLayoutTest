package com.zs.demo.contentlayouttest;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.zs.demo.contentlayouttest.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;

import static com.zs.demo.contentlayouttest.R.id.ll_zhibo_intro_sp_content;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout layout_zhibo_intro;
    private LinearLayout ll_zhibo_intro;
    private LinearLayout ll_zhibo_intro_outline;
    private ImageView iv_zhibo_intro_more;

    private List<Integer> mData = new ArrayList<>();

    // 简介是否展开  0：收起  1：展开
    private boolean mFlagShow;
    // 简介默认高度  210dp
    private int mIntroHeight;
    // 简介布局的高度
    private int mLayoutHeight;

    private Activity mActivity;
    private ViewGroup.LayoutParams mParams;

    private String mText = " HBuilder是一款不错的开发工具，纵观，iOS开发的Xcode，Android开发的ADT、Studio，WP开发的VS。可以下载试玩，免环境安装，即可使用，内置Demo和教程。";
    private String mImage = "https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo/bd_logo1_31bdc765.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActivity = this;

        layout_zhibo_intro = (RelativeLayout) findViewById(R.id.layout_zhibo_intro);
        ll_zhibo_intro = (LinearLayout) findViewById(R.id.ll_zhibo_intro);
        ll_zhibo_intro_outline = (LinearLayout) findViewById(R.id.ll_zhibo_intro_outline);
        iv_zhibo_intro_more = (ImageView) findViewById(R.id.iv_zhibo_intro_more);

        iv_zhibo_intro_more.setOnClickListener(this);

        mIntroHeight = DensityUtil.dip2px(mActivity, 210);
        mData.add(2);
        mData.add(2);

        setDetail();

    }

    private void setDetail(){
        mParams = layout_zhibo_intro.getLayoutParams();
        mParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        mParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layout_zhibo_intro.setLayoutParams(mParams);
        addContent(ll_zhibo_intro_outline,mData);
        ll_zhibo_intro.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        ll_zhibo_intro.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        mLayoutHeight = ll_zhibo_intro.getMeasuredHeight();
                        // 如果高度大于默认高度就收起，小于就展开
                        if (mLayoutHeight <= mIntroHeight) {
                            iv_zhibo_intro_more.setVisibility(View.GONE);
                        } else {
                            mFlagShow = false;
                            mParams.height = mIntroHeight;
                            mParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                            layout_zhibo_intro.setLayoutParams(mParams);
                            iv_zhibo_intro_more.setImageResource(R.mipmap.zhibo_more);
                            iv_zhibo_intro_more.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }


    /**
     * 添加内容
     * @param layout
     * @param intro
     */
    private void addContent(LinearLayout layout, List<Integer> intro){

        layout.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.bottomMargin = DensityUtil.dip2px(mActivity, 8);
        if (intro == null || intro.size() == 0) {
            TextView textView = new TextView(this);
            textView.setText("暂无内容");
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            textView.setLineSpacing(1, 1.25f);
            textView.setTextColor(getResources().getColor(R.color.font_lightgray));
            params.width = LinearLayout.LayoutParams.MATCH_PARENT;
            textView.setLayoutParams(params);
            layout.addView(textView);
        } else {
            for (int i = 0; i < intro.size(); i++) {
                int bean = intro.get(i);
                if (bean == 1) {
                    TextView textView = new TextView(this);
                    textView.setText(mText);
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
                    textView.setLineSpacing(1, 1.25f);
                    textView.setTextColor(getResources().getColor(R.color.font_gray));
                    textView.setLayoutParams(params);
                    layout.addView(textView);

                } else if (bean == 2) {
                    ImageView image = new ImageView(this);
                    // 当图片被盖住的时候，显示图片的顶部
                    image.setScaleType(ImageView.ScaleType.MATRIX);
                    image.setLayoutParams(params);
                    // 一定要设置默认图片或者加个背景图片
                    // 页面首次打开，加载网络图片，获取不到图片的高度
                    Picasso.with(this)
                            .load(mImage)
                            .placeholder(R.drawable.default_image)
                            .into(image);
                    layout.addView(image);
                }
            }
        }


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.iv_zhibo_intro_more:
                if (mFlagShow) { // 收起
                    mFlagShow = false;
                    ViewGroup.LayoutParams params = layout_zhibo_intro.getLayoutParams();
                    params.height = mIntroHeight;
                    params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    layout_zhibo_intro.setLayoutParams(params);
                    iv_zhibo_intro_more.setImageResource(R.mipmap.zhibo_more);
                } else {    // 展开
                    mFlagShow = true;
                    ViewGroup.LayoutParams params = layout_zhibo_intro.getLayoutParams();
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    layout_zhibo_intro.setLayoutParams(params);
                    iv_zhibo_intro_more.setImageResource(R.mipmap.zhibo_more_shouqi);
                }
                break;
        }
    }
}
