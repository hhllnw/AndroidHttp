package com.inthub.baselibrary.view.pull;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.inthub.baselibrary.R;
import com.inthub.baselibrary.common.Logger;


/**
 * Created by hhl on 2016/6/4.
 * RecyclerView、SwipeRefreshLayout 自定义成一个View
 * 支持下拉刷新、上滑加载更多功能
 */
public class PullRecycler extends FrameLayout implements SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private int mCurrentState;//目前状态
    private int ACTION_IDEL = 0;//空闲状态
    public static final int ACTION_PULL_REFRESH = 1;//下拉刷新
    public static final int ACTION_MORE_ACTION = 2;//上滑加载更多
    public static final int LOAD_MORE_CONTIUE = 3;//加载数据，还有数据可以加载
    public static final int LOAD_MORE_END = 4;//加载完所有数据，没有等多数据了
    private boolean isCanLoadMore = false;//是否可以加载 /默认不可以加载更多
    private boolean isCanRefresh = true;//是否可以刷新  /默认可以刷新
    private OnRecyclerRefreshListener listener;
    private ILayoutManager manager;
    private BaselistAdapter adapter;
    /**
     * 加载更多时，默认当倒数五个数出现时加载更多
     */
    private int reachBottomCount = 1;


    /**
     * 刷新、加载回调
     */
    public interface OnRecyclerRefreshListener {
        void onPullRefresh(int action);
    }

    public interface OnClickLastViewListener {
        void onClickLastView();
    }

    public PullRecycler(Context context) {
        super(context);
        initView();
    }

    public PullRecycler(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public PullRecycler(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public void setOnRecyclerRefreshListener(OnRecyclerRefreshListener listener) {
        this.listener = listener;
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.widget_pull_recycler, this, true);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.mSwipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                android.R.color.holo_orange_light, android.R.color.holo_green_light);
        mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mCurrentState == ACTION_IDEL && isCanLoadMore && reachBottom() && dy > 0) {
                    mCurrentState = ACTION_MORE_ACTION;
                    mSwipeRefreshLayout.setEnabled(false);
                    listener.onPullRefresh(ACTION_MORE_ACTION);
                    adapter.showLoadMoreFooter(true);
                }
            }
        });

        mRecyclerView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mCurrentState == ACTION_PULL_REFRESH) {
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    public void scrollToPosition(int position) {
        mRecyclerView.scrollToPosition(position);

    }

    /**
     * 列表滑到底部
     *
     * @return
     */
    private boolean reachBottom() {
        int position = manager.findLastVisiblePosition();
        //getItemCount表示data.size(),getChildCount()表示视图中可见数目
        int totalCount = manager.getLayoutManager().getItemCount();
        return totalCount - position <= reachBottomCount;
    }


    public void setReachBottomViewCount(int reachBottomCount) {
        this.reachBottomCount = reachBottomCount;
    }

    public void setIsCanLoadMore(boolean isCanLoadMore) {
        this.isCanLoadMore = isCanLoadMore;
    }

    public void setIsCanRefresh(boolean isCanRefresh) {
        this.isCanRefresh = isCanRefresh;
        if (isCanRefresh) {
            mSwipeRefreshLayout.setEnabled(true);
        } else {
            mSwipeRefreshLayout.setEnabled(false);
        }
    }

    /**
     * 首次进入页面自动刷新获取列表数据
     */
    public void setFirstRefresh() {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                onRefresh();
            }
        });
    }

    public void setAdapter(BaselistAdapter adapter) {
        this.adapter = adapter;
        mRecyclerView.setAdapter(adapter);
        manager.setUpAdapter(adapter);
    }


    public void setLayoutManager(ILayoutManager manager) {
        this.manager = manager;
        mRecyclerView.setLayoutManager(manager.getLayoutManager());
    }

    @Override
    public void onRefresh() {
        mCurrentState = ACTION_PULL_REFRESH;
        listener.onPullRefresh(ACTION_PULL_REFRESH);
        setFooterTv("正在加载...", VISIBLE);
    }

    public void onComplete() {
        onComplete(LOAD_MORE_CONTIUE);
    }


    public void onComplete(int state) {
        switch (mCurrentState) {
            case ACTION_PULL_REFRESH:
                mSwipeRefreshLayout.setRefreshing(false);
                break;
            case ACTION_MORE_ACTION:
                if (state == LOAD_MORE_END) {
                    setFooterTv("没有更多数据了", GONE);
                } else if (state == LOAD_MORE_CONTIUE) {
                    adapter.showLoadMoreFooter(false);
                }
                if (isCanRefresh) {
                    mSwipeRefreshLayout.setEnabled(true);
                }
                break;
        }
        mCurrentState = ACTION_IDEL;
    }


    public void isShowLastView(boolean isShowLastView, View view) {
        if (adapter != null) {
            adapter.isShowLastView(isShowLastView, view);
        } else {
            Logger.E("err", "adapter is null,please init adapter");
        }

    }

    public void setFooterTv(String str, int look) {
        if (adapter != null) {
            adapter.setFooterTv(str, look);
        }
    }

    public void setFooterTv(String str) {
        if (adapter != null) {
            adapter.setFooterTv(str);
        }
    }

    public void setOnClickLastViewListener(OnClickLastViewListener lastViewListener) {
        if (adapter != null) {
            adapter.setOnClickLastViewListener(lastViewListener);
        } else {
            Logger.E("err", "adapter is null,please init adapter");
        }
    }

}
