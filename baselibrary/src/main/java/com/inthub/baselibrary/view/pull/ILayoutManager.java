package com.inthub.baselibrary.view.pull;

import android.support.v7.widget.RecyclerView;

/**
 * Created by hhl on 2016/6/4.
 */
public interface ILayoutManager {
    /**
     * Returns the adapter position of the last visible view.
     * @return
     */
    int findLastVisiblePosition();

    /**
     * 获取LayoutManager类型
     *
     * @return
     */
    RecyclerView.LayoutManager getLayoutManager();

    void setUpAdapter(BaselistAdapter adapter);
}
