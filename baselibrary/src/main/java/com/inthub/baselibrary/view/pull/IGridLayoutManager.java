package com.inthub.baselibrary.view.pull;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by hhl on 2016/6/5.
 */
public class IGridLayoutManager extends GridLayoutManager implements ILayoutManager {

    public IGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public IGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public IGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    @Override
    public int findLastVisiblePosition() {
        return findLastVisibleItemPosition();
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return this;
    }

    @Override
    public void setUpAdapter(BaselistAdapter adapter) {
        FooterSpanSizeLookup lookup = new FooterSpanSizeLookup(adapter, getSpanCount());
        setSpanSizeLookup(lookup);
    }
}
