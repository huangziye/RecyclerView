package com.hzy.recyclerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by ziye_huang on 2018/8/2.
 */
public class RecyclerDividerItemDecoration extends RecyclerView.ItemDecoration {

    private Drawable mDivider;     //分割线Drawable
    private int mDividerHeight;  //分割线高度
    // 最后 num 条分割线不显示
    private int num = 0;

    /**
     * 使用line_divider中定义好的颜色
     *
     * @param context
     * @param dividerHeight 分割线高度
     */
    public RecyclerDividerItemDecoration(Context context, int dividerHeight) {
        mDivider = ContextCompat.getDrawable(context, R.drawable.item_divider);
        mDividerHeight = dividerHeight;
    }

    public RecyclerDividerItemDecoration(Context context, int dividerHeight, int lastInVisible) {
        mDivider = ContextCompat.getDrawable(context, R.drawable.item_divider);
        mDividerHeight = dividerHeight;
        num = lastInVisible;
    }

    /**
     * @param context
     * @param divider       分割线Drawable
     * @param dividerHeight 分割线高度
     */
    public RecyclerDividerItemDecoration(Context context, Drawable divider, int dividerHeight) {
        if (divider == null) {
            mDivider = ContextCompat.getDrawable(context, R.drawable.item_divider);
        } else {
            mDivider = divider;
        }
        mDividerHeight = dividerHeight;
    }

    //获取分割线尺寸
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(0, 0, 0, mDividerHeight);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount - num; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDividerHeight;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }
}
