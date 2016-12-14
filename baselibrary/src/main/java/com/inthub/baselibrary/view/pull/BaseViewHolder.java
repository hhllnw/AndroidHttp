package com.inthub.baselibrary.view.pull;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by hhl on 2016/5/31.
 * Recycler 基础ViewHolder类
 */
public abstract class BaseViewHolder extends RecyclerView.ViewHolder {

    public BaseViewHolder(final View itemView) {
        super(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnItemClick(itemView, getAdapterPosition());
            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                BaseViewHolder.this.onLongClick(itemView, getAdapterPosition());
                return false;
            }
        });
    }


    /**
     * 数据与控件的绑定
     *
     * @param position
     */
    public abstract void bind(int position);

    /**
     * Item的单击事件
     *
     * @param position
     */
    public abstract void OnItemClick(View view, int position);

    /**
     * item的长按事件
     *
     * @param itemView
     * @param position
     */
    protected void onLongClick(View itemView, int position) {
    }
}
