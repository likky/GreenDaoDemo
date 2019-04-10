package com.dxl.greendaodemo;

import android.content.Context;
import android.view.View;

import com.dxl.greendaodemo.greendao.DaoSession;
import com.dxl.greendaodemo.greendao.Student;

public class MainRecyclerViewAdapter extends CommonRecyclerViewAdapter<Student> {


    public MainRecyclerViewAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    @Override
    protected void bindData(CommonRecyclerViewHolder viewHolder, Student student, int position) {
        viewHolder.setTextView(R.id.tv_id, String.valueOf(student.getStudentNo()));
        viewHolder.setTextView(R.id.tv_name, student.getName());
        viewHolder.setOnClickListener(position, R.id.iv_delete, new CommonRecyclerViewHolder.ListenerWithPosition.OnClickWithPositionListener() {
            @Override
            public void onClick(View view, int position) {
                Student data = getData(position);
                DaoSession daoSession = ((MyApplication) mContext.getApplicationContext()).getDaoSession();
                daoSession.delete(data);
                getDatas().remove(data);
                notifyItemRemoved(position);
            }
        });
    }
}
