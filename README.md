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




