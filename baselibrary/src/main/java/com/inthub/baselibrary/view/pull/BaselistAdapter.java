package com.inthub.baselibrary.view.pull;


import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.inthub.baselibrary.R;
import com.inthub.baselibrary.common.Logger;


/**
 * Created by hhl on 2016/6/27.
 */
public abstract class BaselistAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    protected boolean isShowFooter = false;
    protected boolean isShowInsertLastView = false;
    private static final int LOAD_MORE_SHOW_FOOTER = 100;
    private static final int INSERT_SHOW_LAST_VIEW = 101;
    private View lastView;//recycler新添加的最后一个view
    private PullRecycler.OnClickLastViewListener lastViewListener;

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == LOAD_MORE_SHOW_FOOTER) {
            return addFooterView(parent);
        } else if (viewType == INSERT_SHOW_LAST_VIEW) {
            return insertLastView();
        }
        return onCreateNormalViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if (isShowFooter && (position == getItemCount() - 1)) {
            if (holder.itemView.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
                params.setFullSpan(true);
            }
        }
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return getDataCount() + (isShowFooter ? 1 : 0) + (isShowInsertLastView ? 1 : 0);
    }


    @Override
    public int getItemViewType(int position) {
        if (isShowFooter && position == (getItemCount() - 1)) {
            return LOAD_MORE_SHOW_FOOTER;
        } else if (isShowInsertLastView && position == (getItemCount() - 1)) {
            return INSERT_SHOW_LAST_VIEW;
        }
        return getDataType(position);
    }

    protected int getDataType(int position) {
        return 0;
    }

    public void showLoadMoreFooter(boolean isShow) {
        isShowFooter = isShow;
        if (isShow) {
            notifyItemInserted(getItemCount());
        } else {
            notifyItemRemoved(getItemCount());
        }
    }

    public void isShowLastView(boolean isShow, View view) {
        isShowInsertLastView = isShow;
        lastView = view;
        if (isShowInsertLastView) {
            notifyItemInserted(getDataCount());
        }
    }

    public void setOnClickLastViewListener(PullRecycler.OnClickLastViewListener lastViewListener) {
        this.lastViewListener = lastViewListener;
    }


    public boolean isShowFooter(int position) {
        if (isShowFooter && (position == getItemCount() - 1)) {
            return true;
        }
        return false;
    }

    protected BaseViewHolder addFooterView(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_footer, parent, false);
        tv_footer = (TextView) view.findViewById(R.id.tv_footer);
        mFooterProgressBar = (ProgressBar) view.findViewById(R.id.footer_progressBar);
        return new createLoadMoreFooterViewHolder(view);
    }

    private TextView tv_footer;
    private ProgressBar mFooterProgressBar;

    protected void setFooterTv(String str) {
        if (tv_footer == null || mFooterProgressBar == null) return;
        tv_footer.setText(str);
        mFooterProgressBar.setVisibility(View.GONE);
    }

    protected void setFooterTv(String str, int look) {
        if (tv_footer == null || mFooterProgressBar == null) return;
        tv_footer.setText(str);
        mFooterProgressBar.setVisibility(look);
    }

    private BaseViewHolder insertLastView() {
        if (lastView == null) {
            Logger.E("error", "in PullRecycler.isShowLastView(true,view),view not null ");
            return null;
        } else {
            return new createLastViewHolder(lastView);
        }

    }

    /**
     * 绑定列表Item布局
     *
     * @param parent
     * @param viewType
     * @return
     */
    protected abstract BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType);

    /**
     * 获取数据个数
     *
     * @return
     */
    protected abstract int getDataCount();

    public boolean isSectionHeader(int position) {
        return false;
    }

    protected class createLoadMoreFooterViewHolder extends BaseViewHolder {
        public createLoadMoreFooterViewHolder(View view) {
            super(view);
        }

        @Override
        public void bind(int position) {

        }

        @Override
        public void OnItemClick(View view, int position) {
        }

        @Override
        protected void onLongClick(View itemView, int adapterPosition) {

        }
    }

    protected class createLastViewHolder extends BaseViewHolder {
        public createLastViewHolder(View view) {
            super(view);
        }

        @Override
        public void bind(int position) {

        }

        @Override
        public void OnItemClick(View view, int position) {
            if (lastViewListener != null) {
                lastViewListener.onClickLastView();
            }
        }

        @Override
        protected void onLongClick(View itemView, int adapterPosition) {

        }
    }


}
