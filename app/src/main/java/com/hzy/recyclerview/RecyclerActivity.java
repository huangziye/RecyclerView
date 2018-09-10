package com.hzy.recyclerview;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ziye_huang on 2018/9/7.
 */
public class RecyclerActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView mRecyclerView;
    private List<Bean> mData;
    private int pageIndex = 0;
    private int pageSize = 10;
    private MyAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
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
        mAdapter = new MyAdapter(R.layout.recycler_item_layout, mData);
        setListener();
        onRefresh();
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setListener() {
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(RecyclerActivity.this, "item" + position, Toast.LENGTH_SHORT).show();
            }
        });
        //设置加载更多监听
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
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
            mAdapter.setNewData(data);
            mSwipeRefreshLayout.setRefreshing(false);
        } else {
            if (size > 0) {
                mAdapter.addData(data);
            }
        }
        if (size < pageSize) {
            //第一页如果不够一页就不显示没有更多数据布局
            mAdapter.loadMoreEnd(isRefresh);
            mAdapter.setEmptyView(getLayoutInflater().inflate(R.layout.item_empty, null));
        } else {
            mAdapter.loadMoreComplete();
        }
    }
}
