package com.hynet.heebit.components.widget.sticky.listener;import androidx.recyclerview.widget.RecyclerView;public interface OnViewBinderListener {    void bind(RecyclerView.ViewHolder viewHolder, Object t, int position, boolean checkable);    RecyclerView.ViewHolder getViewHolder(int type);}