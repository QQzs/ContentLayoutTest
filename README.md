# ContentLayoutTest
# 图文混排内容展开收起

## 效果图
![](https://github.com/QQzs/Image/blob/master/ContentLayoutTest/content_anim_show.gif)<br>
发长微博时，内容过多，在显示的时候，会有展开收起状态，这个项目就是解决展示内容是图文混排的情况，上面是效果图。<br>
把要显示的数据，动态的添加，设置一个临界的高度，当要显示的数据不够这个高度，就不显示收起展开的按钮，如果要显示的内容超出这个临界高度，
就显示这个高度，多余的内容遮盖，展开和收起布局有动态的效果。

布局文件：
```Java
<RelativeLayout
        android:id="@+id/layout_zhibo_intro"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:background="@color/white"
        android:orientation="vertical"
        android:paddingLeft="14dp"
        android:paddingRight="14dp">

        <LinearLayout
            android:id="@+id/ll_zhibo_intro"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical"
            android:paddingBottom="28dp">

            <TextView
                android:id="@+id/tv_title"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:drawableLeft="@mipmap/zhibo_xilieke_jianjie_icon"
                android:drawablePadding="9dp"
                android:paddingBottom="8dp"
                android:paddingTop="12dp"
                android:text="简介"
                android:textColor="@color/default_text"
                android:textSize="@dimen/fontsize15" />

            <LinearLayout
                android:id="@+id/ll_zhibo_intro_outline"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:gravity="center_horizontal"
                android:orientation="vertical">

                <TextView
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_marginBottom="8dp"
                    android:lineSpacingMultiplier="1.25"
                    android:text="暂无内容"
                    android:textColor="@color/font_lightgray"
                    android:textSize="@dimen/fontsize14" />
            </LinearLayout>

        </LinearLayout>

        <LinearLayout
            android:id="@+id/ll_zhibo_intro_more"
            android:layout_width="match_parent"
            android:layout_height="28dp"
            android:gravity="center"
            android:layout_alignBottom="@+id/ll_zhibo_intro">

            <ImageView
                android:id="@+id/iv_zhibo_intro_more"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:src="@mipmap/zhibo_more"/>

            <TextView
                android:id="@+id/tv_zhibo_intro_more"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_centerHorizontal="true"
                android:text="展开"
                android:layout_marginLeft="8dp"/>

        </LinearLayout>

    </RelativeLayout>
```
## 代码部分：
首先是判断内容高度部分，需要注意的是不能直接用View.getHeight()、View.getMeasuredWidth()去获取高度，因为是动态添加数据的view，数据，获取的高度肯定是错的，这里采用的是getViewTreeObserver().addOnGlobalLayoutListener方式。
```Java
private void setDetail(){
        mParams = layout_zhibo_intro.getLayoutParams();
        mParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        mParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layout_zhibo_intro.setLayoutParams(mParams);
        addContent(ll_zhibo_intro_outline,mData);
        // 如果高度大于默认高度就收起，小于就展开
        ll_zhibo_intro.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        ll_zhibo_intro.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        mHeight = getLayoutHeight();
                        if (mHeight <= mIntroHeight) {
                            tv_zhibo_intro_more.setVisibility(View.GONE);
                        } else {
                            mFlagShow = false;
                            mParams.height = mIntroHeight;
                            mParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                            layout_zhibo_intro.setLayoutParams(mParams);
                                tv_zhibo_intro_more.setVisibility(View.VISIBLE);
                            setMoreLayout();
                        }
                    }
                });
    }
```
然后是添加要显示的图文内容：
需要注意的是图片的显示样式ScaleType，采用的是MATRIX，显示图片的顶部的内容，关于ScaleType，参考链接：
[ScaleType](https://www.2cto.com/kf/201411/348601.html)
```Java
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
                    params.width = LinearLayout.LayoutParams.MATCH_PARENT;
                    TextView textView = new TextView(this);
                    textView.setText(mText);
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
                    textView.setLineSpacing(1, 1.25f);
                    textView.setTextColor(getResources().getColor(R.color.font_gray));
                    textView.setLayoutParams(params);
                    layout.addView(textView);

                } else if (bean == 2) {
                    params.width = LinearLayout.LayoutParams.WRAP_CONTENT;
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
    }
```
获取全部内容的高度这个地方，一定要算的准确了，这个项目中计算的是标题的高度加上给底部展开按钮空出的高度，再加上每一段文字或者图片的高度，和每一段之间空出的8dp的高度，要添加的图片数据的中要包括图片的高度，如果是服务器中返回的也要带着高度。
```Java
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
```
下面是添加的动画效果：
```Java
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
```


