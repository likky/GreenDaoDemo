package com.dxl.greendaodemo;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

public class CommonRecyclerViewHolder extends RecyclerView.ViewHolder {

    private SparseArray<View> mViews;


    public CommonRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        mViews = new SparseArray<>();
    }


    public <T extends View> T getView(@IdRes int id) {
        View view = mViews.get(id);
        if (view == null) {
            view = itemView.findViewById(id);
            mViews.put(id, view);
        }
        return (T) view;
    }

    public void setOnClickListener(int position, @IdRes int id, ListenerWithPosition.OnClickWithPositionListener listener) {
        ListenerWithPosition listenerWithPosition = new ListenerWithPosition(position, listener);
        getView(id).setOnClickListener(listenerWithPosition);
    }

    public void setOnClickListener(int position, ListenerWithPosition.OnClickWithPositionListener listener) {
        ListenerWithPosition listenerWithPosition = new ListenerWithPosition(position, listener);
        itemView.setOnClickListener(listenerWithPosition);
    }

    public void setTextView(@IdRes int id, String text) {
        View view = getView(id);
        if (view instanceof TextView) {
            ((TextView) view).setText(text);
        }
    }

    public static class ListenerWithPosition implements View.OnClickListener {

        int mPosition;
        OnClickWithPositionListener mOnClickWithPositionListener;

        public ListenerWithPosition(int position, OnClickWithPositionListener onClickWithPositionListener) {
            mPosition = position;
            mOnClickWithPositionListener = onClickWithPositionListener;
        }

        public interface OnClickWithPositionListener {
            void onClick(View view, int position);
        }

        @Override
        public void onClick(View v) {
            if (mOnClickWithPositionListener != null) {
                mOnClickWithPositionListener.onClick(v, mPosition);
            }
        }
    }
}
