package com.hzy.recyclerview;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemDragListener;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ziye_huang on 2018/9/7.
 */
public class DragActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = DragActivity.class.getSimpleName();
    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView mRecyclerView;
    ItemDragAdapter mItemDragAdapter;
    OnItemDragListener mOnItemDragListener;
    ItemDragAndSwipeCallback mItemDragAndSwipeCallback;
    ItemTouchHelper mItemTouchHelper;
    OnItemSwipeListener mOnItemSwipeListener;
    private int pageIndex = 0;
    private int pageSize = 10;
    List<Bean> mData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag);
        initView();
        initAdapter();
    }

    private void initView() {
        mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        mRecyclerView = findViewById(R.id.recyclerView);
        //设置下拉刷新监听
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_red_light, android.R.color.holo_orange_light);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new RecyclerDividerItemDecoration(this, 2));
    }

    private void initAdapter() {
        mItemDragAdapter = new ItemDragAdapter(mData);
        setListener();
        onRefresh();
        mRecyclerView.setAdapter(mItemDragAdapter);

        mItemDragAndSwipeCallback = new ItemDragAndSwipeCallback(mItemDragAdapter);
        mItemTouchHelper = new ItemTouchHelper(mItemDragAndSwipeCallback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

        mItemDragAndSwipeCallback.setSwipeMoveFlags(ItemTouchHelper.START | ItemTouchHelper.END);
        mItemDragAdapter.enableSwipeItem();
        mItemDragAdapter.setOnItemSwipeListener(mOnItemSwipeListener);
        mItemDragAdapter.enableDragItem(mItemTouchHelper);
    }

    private void setListener() {
        mOnItemDragListener = new OnItemDragListener() {
            @Override
            public void onItemDragStart(RecyclerView.ViewHolder viewHolder, int pos) {
                BaseViewHolder holder = ((BaseViewHolder) viewHolder);
                System.err.println("............................onItemDragStart.............................");
            }

            @Override
            public void onItemDragMoving(RecyclerView.ViewHolder source, int from, RecyclerView.ViewHolder target, int to) {
                Log.d(TAG, "move from: " + source.getAdapterPosition() + " to: " + target.getAdapterPosition());
                System.err.println("............................onItemDragMoving.............................");
            }

            @Override
            public void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int pos) {
                BaseViewHolder holder = ((BaseViewHolder) viewHolder);
                System.err.println("............................onItemDragEnd.............................");
            }
        };
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(20);
        paint.setColor(Color.BLACK);
        mOnItemSwipeListener = new OnItemSwipeListener() {
            @Override
            public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {
                BaseViewHolder holder = ((BaseViewHolder) viewHolder);
            }

            @Override
            public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {
                BaseViewHolder holder = ((BaseViewHolder) viewHolder);
            }

            @Override
            public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos) {
                BaseViewHolder holder = ((BaseViewHolder) viewHolder);
            }

            @Override
            public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive) {
                BaseViewHolder holder = ((BaseViewHolder) viewHolder);
                canvas.drawColor(ContextCompat.getColor(DragActivity.this, R.color.colorAccent));
            }
        };

        mItemDragAdapter.setOnItemDragListener(mOnItemDragListener);
        mItemDragAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(DragActivity.this, "item" + position, Toast.LENGTH_SHORT).show();
            }
        });
        //设置加载更多监听
        mItemDragAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setData(false, mData = getData());
                    }
                }, 2000);
            }
        }, mRecyclerView);
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pageIndex = 0;
                setData(true, getData());
            }
        }, 2000);
    }

    private List<Bean> getData() {
        List<Bean> list = new ArrayList<>();
        for (int i = pageIndex * pageSize; i < pageIndex * pageSize + pageSize; i++) {
            list.add(new Bean("item" + i));
        }
        if (pageIndex > 3) {
            list.clear();
        }
        return list;
    }

    private void setData(boolean isRefresh, List<Bean> data) {
        pageIndex++;
        final int size = data == null ? 0 : data.size();
        if (isRefresh) {
            mItemDragAdapter.setNewData(data);
            mSwipeRefreshLayout.setRefreshing(false);
        } else {
            if (size > 0) {
                mItemDragAdapter.addData(data);
            }
        }
        if (size < pageSize) {
            //第一页如果不够一页就不显示没有更多数据布局
            mItemDragAdapter.loadMoreEnd(isRefresh);
            mItemDragAdapter.setEmptyView(getLayoutInflater().inflate(R.layout.item_empty, null));
        } else {
            mItemDragAdapter.loadMoreComplete();
        }
    }
}
