# ContentLayoutTest
# 图文混排内容展开收起

## 效果图
![](https://github.com/QQzs/Image/blob/master/ContentLayoutTest/content_layout_show.gif)<br>
发长微博时，内容过多，在显示的时候，会有展开收起状态，这个项目就是解决展示内容是图文混排的情况，上面是效果图。<br>
把要显示的数据，动态的添加，设置一个临界的高度，当要显示的数据不够这个高度，就不显示收起展开的按钮，如果要显示的内容超出这个临界高度，
就显示这个高度，多余的内容遮盖。主要特殊的处理是在显示遮盖图片的地方。

布局文件：
```Java
<RelativeLayout
        android:id="@+id/layout_zhibo_intro"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="12dp"
        android:background="@color/white"
        android:paddingLeft="14dp"
        android:paddingRight="14dp"
        android:animateLayoutChanges="true"
        android:orientation="vertical"
        android:visibility="visible"
        android:layout_alignParentTop="true"
        android:layout_alignParentLeft="true"
        android:layout_alignParentStart="true">

        <LinearLayout
            android:id="@+id/ll_zhibo_intro"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical"
            android:visibility="visible"
            android:paddingBottom="28dp">
            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginTop="12dp"
                android:drawableLeft="@mipmap/zhibo_xilieke_jianjie_icon"
                android:drawablePadding="9dp"
                android:text="简介"
                android:textColor="@color/default_text"
                android:textSize="@dimen/fontsize15" />

            <LinearLayout
                android:id="@+id/ll_zhibo_intro_outline"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginTop="8dp"
                android:gravity="center_horizontal"
                android:orientation="vertical">

                <TextView
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:textSize="@dimen/fontsize14"
                    android:layout_marginBottom="8dp"
                    android:lineSpacingMultiplier="1.25"
                    android:text="暂无内容"
                    android:textColor="@color/font_lightgray"
                    />

            </LinearLayout>

        </LinearLayout>

        <TextView
            android:id="@+id/tv_zhibo_intro_more"
            android:layout_width="wrap_content"
            android:layout_height="28dp"
            android:paddingBottom="6dp"
            android:layout_centerHorizontal="true"
            android:layout_alignBottom="@+id/ll_zhibo_intro"
            android:text="展开"
            android:drawableLeft="@mipmap/zhibo_more"
            android:drawablePadding="6dp"
            android:visibility="visible"
            />

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
                        mLayoutHeight = ll_zhibo_intro.getMeasuredHeight();
                        if (mLayoutHeight <= mIntroHeight) {
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
[](https://www.2cto.com/kf/201411/348601.html)
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
        Drawable drawable;
        if (mFlagShow){
            tv_zhibo_intro_more.setText("收起");
            drawable = getResources().getDrawable(R.mipmap.zhibo_more_shouqi);
        }else{
            tv_zhibo_intro_more.setText("展开");
            drawable = getResources().getDrawable(R.mipmap.zhibo_more);
        }
        // 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tv_zhibo_intro_more.setCompoundDrawables(drawable,null,null,null);
    }
```
## 不足之处
没加展开收起的动态效果，比较生硬


