package com.dxl.greendaodemo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class CommonRecyclerViewAdapter<T> extends RecyclerView.Adapter<CommonRecyclerViewHolder> {

    private List<T> mList;
    protected Context mContext;
    private int mLayoutId;

    private MuiltipleTypeSupport mTypeSupport;

    public CommonRecyclerViewAdapter(Context context, int layoutId) {
        mContext = context;
        mLayoutId = layoutId;
        mList = new ArrayList<>();
    }

    public CommonRecyclerViewAdapter(Context context, int layoutId, MuiltipleTypeSupport typeSupport) {
        this(context, layoutId);
        mTypeSupport = typeSupport;
    }

    public void addData(T t) {
        mList.add(t);
        notifyItemInserted(mList.size() - 1);
    }

    public void addDatas(List<T> ts) {
        mList.addAll(ts);
        notifyItemRangeInserted(mList.size() - ts.size(), mList.size());
    }

    public void setDatas(List<T> ts) {
        mList.clear();
        mList.addAll(ts);
        notifyDataSetChanged();
    }

    public List<T> getDatas() {
        return mList;
    }

    public T getData(int position){
        return mList.get(position);
    }


    @NonNull
    @Override
    public CommonRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (mTypeSupport != null) {
            int layoutId = mTypeSupport.getLayoutId(viewType);
            view = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
        }else {
            view = LayoutInflater.from(mContext).inflate(mLayoutId,parent, false);
        }
        return new CommonRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommonRecyclerViewHolder holder, int position) {
        bindData(holder, getData(position), position);
    }

    protected abstract void bindData(CommonRecyclerViewHolder viewHolder, T t, int position);

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public interface MuiltipleTypeSupport<T> {
        int getLayoutId(int viewType);

        int getItemViewType(int postition, T t);
    }
}
