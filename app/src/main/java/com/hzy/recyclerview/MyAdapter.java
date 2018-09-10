package com.hzy.recyclerview;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

class MyAdapter extends BaseQuickAdapter<Bean, BaseViewHolder> {
    public MyAdapter(int layoutResId, @Nullable List<Bean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Bean item) {
        helper.setText(R.id.tv_name, item.getName());
    }
}