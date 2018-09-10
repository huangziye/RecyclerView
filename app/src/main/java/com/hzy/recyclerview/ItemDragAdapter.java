package com.hzy.recyclerview;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by ziye_huang on 2018/9/7.
 */
public class ItemDragAdapter extends BaseItemDraggableAdapter<Bean, BaseViewHolder> {
    public ItemDragAdapter(List<Bean> data) {
        super(R.layout.item_drag_layout, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Bean item) {
        helper.setText(R.id.tv, item.getName());
    }
}
