package com.bhp.memo_book.memo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bhp.memo_book.R;
import com.bhp.memo_book.databinding.ActivityAddPageBinding;
import com.bhp.memo_book.ui.BaseActivity;
import com.bhp.memo_book.utils.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NewMatterActivity extends BaseActivity {
    DatabaseHelper helper;
    String[] style_option = {"普通", "紧急", "待办"};

    private ActivityAddPageBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_page);
        helper = DatabaseHelper.getInstance(this);
        final ArrayAdapter<String> styleAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, style_option);
        binding.style.setAdapter(styleAdapter);
        final int style_value = 0;
        binding.style.setSelection(style_value, true);
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timer_value = dateFormat.format(now);
        binding.timer.setText(timer_value);
        final SharedPreferences sp = getSharedPreferences("data", Context.MODE_PRIVATE);
        //退回
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewMatterActivity.this, MemoBookActivity.class);
                startActivity(intent);
                NewMatterActivity.this.finish();
            }
        });
        //添加
        binding.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String head_value = binding.head.getText().toString();
                String timer_value = binding.timer.getText().toString();
                int style_value;
                String choice = binding.style.getSelectedItem().toString();
                String name = sp.getString("username", "");
                if (name == "") {
                    Toast.makeText(NewMatterActivity.this, "获取用户名异常", Toast.LENGTH_SHORT).show();
                } else {
                    switch (choice) {
                        case "紧急":
                            style_value = 1;
                            break;
                        case "待办":
                            style_value = 2;
                            break;
                        default:
                            style_value = 0;
                    }
                    String body_value = binding.body.getText().toString();
                    final SQLiteDatabase db = helper.getWritableDatabase();
                    String sql = String.format("insert into res(_NAME,_DATE,_HEAD,_MSG,_TYPE) values('%s','%s','%s','%s',%d);", name, timer_value, head_value, body_value, style_value);
                    try {
                        db.execSQL(sql);
                        Toast.makeText(NewMatterActivity.this, "添加成功", Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        Toast.makeText(NewMatterActivity.this, "添加失败", Toast.LENGTH_SHORT).show();
                    }
                    Intent intent = new Intent(NewMatterActivity.this, MemoBookActivity.class);
                    startActivity(intent);
                    NewMatterActivity.this.finish();
                }
            }
        });
    }
}
