package com.zs.demo.contentlayouttest;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zs.demo.contentlayouttest.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * @author zs
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout layout_zhibo_intro;
    private LinearLayout ll_zhibo_intro;
    private LinearLayout ll_zhibo_intro_outline;
    private TextView tv_title;
    private LinearLayout ll_zhibo_intro_more;
    private ImageView iv_zhibo_intro_more;
    private TextView tv_zhibo_intro_more;

    /**
     * 简介是否展开  0：收起  1：展开
     */
    private boolean mFlagShow;
    /**
     * 简介默认高度  210dp
     */
    private int mIntroHeight;
    /**
     * 简介全部内容的高度
     */
    private int mHeight = 0;

    private Activity mActivity;
    private ViewGroup.LayoutParams mParams;
    private List<Integer> mData = new ArrayList<>();

    private String mText = " HBuilder是一款不错的开发工具，纵观，iOS开发的Xcode，Android开发的ADT、Studio，WP开发的VS。可以下载试玩，免环境安装，即可使用，内置Demo和教程。";
    private String mImage = "https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=4126321865,2582840083&fm=173&s=D096189A578546EEDE75B9D003009035&w=622&h=485&img.PNG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActivity = this;

        layout_zhibo_intro = (RelativeLayout) findViewById(R.id.layout_zhibo_intro);
        ll_zhibo_intro = (LinearLayout) findViewById(R.id.ll_zhibo_intro);
        ll_zhibo_intro_outline = (LinearLayout) findViewById(R.id.ll_zhibo_intro_outline);
        tv_title = (TextView) findViewById(R.id.tv_title);
        ll_zhibo_intro_more = (LinearLayout) findViewById(R.id.ll_zhibo_intro_more);
        iv_zhibo_intro_more = (ImageView) findViewById(R.id.iv_zhibo_intro_more);
        tv_zhibo_intro_more = (TextView) findViewById(R.id.tv_zhibo_intro_more);

        ll_zhibo_intro_more.setOnClickListener(this);

        mIntroHeight = DensityUtil.dip2px(mActivity, 210);
        mData.add(1);
        mData.add(2);
        mData.add(1);

        setDetail();

    }

    private void setDetail(){
        mParams = layout_zhibo_intro.getLayoutParams();
        mParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        mParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layout_zhibo_intro.setLayoutParams(mParams);
        addContent(ll_zhibo_intro_outline,mData);

        Log.d("My_Log","he == "+ ll_zhibo_intro.getMeasuredHeight());
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        ll_zhibo_intro.measure(h, 0);
        Log.d("My_Log","he over == "+ ll_zhibo_intro.getMeasuredHeight());

        // 如果高度大于默认高度就收起，小于就展开
        ll_zhibo_intro.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        ll_zhibo_intro.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        mHeight = getLayoutHeight();
                        if (mHeight <= mIntroHeight) {
                            ll_zhibo_intro_more.setVisibility(View.GONE);
                        } else {
                            mFlagShow = false;
                            mParams.height = mIntroHeight;
                            mParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                            layout_zhibo_intro.setLayoutParams(mParams);
                            ll_zhibo_intro_more.setVisibility(View.VISIBLE);
                            setMoreLayout();
                        }
                    }
                });
    }

    /**
     * 获取全部内容的高度
     * @return
     */
    private int getLayoutHeight(){
        int height = tv_title.getMeasuredHeight() + DensityUtil.dip2px(this,28) + ll_zhibo_intro_outline.getChildCount() * DensityUtil.dip2px(this,8);
        for (int i = 0; i< ll_zhibo_intro_outline.getChildCount(); i ++ ){
            if (mData.get(i) == 1){
                height += ll_zhibo_intro_outline.getChildAt(i).getMeasuredHeight();
            }else{
                // demo中图片高度，按实际为准
                height += 485;
            }
        }
        return height;
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
                    // 页面首次打开，加载网络图片，获取不到图片的高度
                    // 设置默认图片或者加个背景图片占着高度
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
            case R.id.ll_zhibo_intro_more:
                if (mFlagShow) { // 收起
                    mFlagShow = false;
//                    ViewGroup.LayoutParams params = layout_zhibo_intro.getLayoutParams();
//                    params.height = mIntroHeight;
//                    params.width = ViewGroup.LayoutParams.MATCH_PARENT;
//                    layout_zhibo_intro.setLayoutParams(params);
                    animateClose(layout_zhibo_intro);
                } else {    // 展开
                    mFlagShow = true;
//                    ViewGroup.LayoutParams params = layout_zhibo_intro.getLayoutParams();
//                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//                    params.width = ViewGroup.LayoutParams.MATCH_PARENT;
//                    layout_zhibo_intro.setLayoutParams(params);
                    animateOpen(layout_zhibo_intro);
                }
                setMoreLayout();
                break;
        }
    }

    /**
     * 设置收起展开状态
     */
    private void setMoreLayout(){
        if (mFlagShow){
            tv_zhibo_intro_more.setText("收起");
            animationIvOpen();
        }else{
            tv_zhibo_intro_more.setText("展开");
            animationIvClose();
        }
//        Drawable drawable;
//        if (mFlagShow){
//            tv_zhibo_intro_more.setText("收起");
//            drawable = getResources().getDrawable(R.mipmap.zhibo_more_shouqi);
//        }else{
//            tv_zhibo_intro_more.setText("展开");
//            drawable = getResources().getDrawable(R.mipmap.zhibo_more);
//        }
        // 这一步必须要做,否则不会显示.
//        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//        tv_zhibo_intro_more.setCompoundDrawables(drawable,null,null,null);
    }

    /**
     * 展开布局
     * @param view
     */
    private void animateOpen(View view) {
        ValueAnimator animator = createDropAnimator(view, mIntroHeight,mHeight);
        animator.start();
    }

    /**
     * 收起布局
     * @param view
     */
    private void animateClose(final View view) {
        ValueAnimator animator = createDropAnimator(view, mHeight, mIntroHeight);
        animator.start();
    }

    private ValueAnimator createDropAnimator(final View v, int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.setDuration(1000);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator arg0) {
                int value = (int) arg0.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
                layoutParams.height = value;
                v.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

    /**
     * view展开 图标旋转动画
     */
    private void animationIvOpen() {
        RotateAnimation animation = new RotateAnimation(0, 180,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        animation.setFillAfter(true);
        animation.setDuration(300);
        iv_zhibo_intro_more.startAnimation(animation);
    }

    /**
     * view收起 图标旋转动画
     */
    private void animationIvClose() {
        RotateAnimation animation = new RotateAnimation(180, 0,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        animation.setFillAfter(true);
        animation.setDuration(200);
        iv_zhibo_intro_more.startAnimation(animation);
    }
}