package com.inthub.baselibrary.view.pull;


import android.support.v7.widget.GridLayoutManager;


public class FooterSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {
    private BaselistAdapter adapter;
    private int spanCount;

    public FooterSpanSizeLookup(BaselistAdapter adapter, int spanCount) {
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
