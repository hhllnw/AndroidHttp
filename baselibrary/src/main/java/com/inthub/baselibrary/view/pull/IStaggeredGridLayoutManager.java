package com.inthub.baselibrary.view.pull;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;

/**
 * Created by hhl on 2016/6/5.
 */
public class IStaggeredGridLayoutManager extends StaggeredGridLayoutManager implements ILayoutManager {

    public IStaggeredGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public IStaggeredGridLayoutManager(int spanCount, int orientation) {
        super(spanCount, orientation);
    }

    @Override
    public int findLastVisiblePosition() {
        int[] positions = null;
        positions = findLastVisibleItemPositions(positions);
        return positions[0];
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return this;
    }

    @Override
    public void setUpAdapter(BaselistAdapter adapter) {

    }
}
