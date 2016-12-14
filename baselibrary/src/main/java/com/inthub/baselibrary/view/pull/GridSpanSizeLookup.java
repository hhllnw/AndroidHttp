package com.inthub.baselibrary.view.pull;

import android.support.v7.widget.GridLayoutManager;


/**
 * Created by hhl on 2016/6/26.
 * <p/>
 * 当LayoutManager属于IGridLayoutManager 类型时，列表加载更多时，footerView是否占据一行
 * A helper class to provide the number of spans each item occupies.
 * Default implementation sets each item to occupy exactly 1 span.(默认占一列)
 */
public class GridSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {
    private BaselistAdapter adapter;
    /**
     * new IGridLayoutManager(this, 3);例如在new IGridLayoutManager对象时，span==3
     * 如果在加载更多时，这里的spanCount设置成3，那么footerView将占据一行
     */
    private int spanCount;

    public GridSpanSizeLookup(BaselistAdapter adapter, int spanCount) {
        this.adapter = adapter;
        this.spanCount = spanCount;
    }

    @Override
    public int getSpanSize(int position) {
        if (adapter.isShowFooter(position) || adapter.isSectionHeader(position)) {
            return spanCount;
        }
        return 1;
    }
}
