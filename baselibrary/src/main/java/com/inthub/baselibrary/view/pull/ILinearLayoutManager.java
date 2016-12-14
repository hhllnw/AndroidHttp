package com.inthub.baselibrary.view.pull;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by hhl on 2016/6/4.
 */
public class ILinearLayoutManager extends LinearLayoutManager implements ILayoutManager {

    public ILinearLayoutManager(Context context) {
        super(context);
    }

    public ILinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public ILinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
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

    }

}
