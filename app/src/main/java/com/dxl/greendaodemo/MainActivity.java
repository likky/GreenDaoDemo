package com.dxl.greendaodemo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.dxl.greendaodemo.greendao.DaoSession;
import com.dxl.greendaodemo.greendao.Student;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private Button button;

    private DaoSession daoSession;
    private MainRecyclerViewAdapter mAdapter;
    private EditText mEditId;
    private EditText mEditName;
    private RadioGroup mRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initDatabase();
        button.setOnClickListener(this);
    }


    private void initDatabase() {
        daoSession = ((MyApplication) getApplication()).getDaoSession();
        List<Student> students = daoSession.loadAll(Student.class);
        mAdapter.setDatas(students);
    }

    private void initView() {
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        button = findViewById(R.id.button);

        mEditId = findViewById(R.id.et_id);
        mEditName = findViewById(R.id.et_name);
        mRadioGroup = findViewById(R.id.radioGroup);

        mAdapter = new MainRecyclerViewAdapter(this, R.layout.item_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);


    }

    @Override
    public void onClick(View view) {
        if (mEditId.getText().toString().isEmpty()
                || mEditName.getText().toString().isEmpty()) {
            new AlertDialog.Builder(this).setTitle("提示")
                    .setMessage("请输入内容")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).show();
            return;
        }
        Student student = new Student();
        int studentNo = Integer.valueOf(mEditId.getText().toString());
        student.setStudentNo(studentNo);
        student.setName(mEditName.getText().toString());
        int sexIndex = 0;
        for (int i = 0; i < mRadioGroup.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) mRadioGroup.getChildAt(i);
            if (radioButton.isChecked()) {
                sexIndex = i;
                break;
            }
        }
        student.setSex(sexIndex == 0 ? "男" : "女");
        List<Student> students = daoSession.queryRaw(Student.class, "where STUDENT_NO=?", studentNo + "");
        if (students != null && students.size() > 0) {
            showSameMessage(student);
        } else {
            daoSession.insert(student);
            mAdapter.addData(student);
            clearInput();
        }
    }

    private void showSameMessage(final Student student) {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("存在相同项，是否替换？")
                .setPositiveButton("替换", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        daoSession.insertOrReplace(student);
                        List<Student> students = daoSession.loadAll(Student.class);
                        mAdapter.setDatas(students);
                        clearInput();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

    private void clearInput() {
        mEditId.setText("");
        mEditName.setText("");
    }
}
