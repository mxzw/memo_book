package com.bhp.memo_book.memo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
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
import com.bhp.memo_book.databinding.ActivityEditPageBinding;
import com.bhp.memo_book.ui.BaseActivity;
import com.bhp.memo_book.utils.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MatterDetailsActivity extends BaseActivity {

    DatabaseHelper helper;
    String[] style_option = {"普通", "紧急", "待办"};
    String ortime;
    int style_value;

    private ActivityEditPageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_page);
        helper = DatabaseHelper.getInstance(this);
        final ArrayAdapter<String> styleAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, style_option);
        binding.style.setAdapter(styleAdapter);
        final Date now = new Date();
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timer_value = dateFormat.format(now);
        final SharedPreferences sp = getSharedPreferences("data", Context.MODE_PRIVATE);
        ortime = sp.getString("time", "");
        binding.timer.setText(ortime);
        //初始化
        final SQLiteDatabase db = helper.getWritableDatabase();
        String init = String.format("select _HEAD,_MSG,_TYPE from res where _DATE='%s';", ortime);
        Cursor cursor = db.rawQuery(init, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                binding.head.setText(cursor.getString(0));
                binding.body.setText(cursor.getString(1));
                style_value = cursor.getInt(2);
            }
        }
        binding.style.setSelection(style_value, true);
        //退回
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MatterDetailsActivity.this, MemoBookActivity.class);
                startActivity(intent);
                MatterDetailsActivity.this.finish();
            }
        });
        //修改
        binding.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String head_value = binding.head.getText().toString();

                int style_value;
                String choice = binding.style.getSelectedItem().toString();
                String name = sp.getString("username", "");
                if (name == "") {
                    Toast.makeText(MatterDetailsActivity.this, "获取用户名异常", Toast.LENGTH_SHORT).show();
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
                    String sql = String.format("update res set _DATE='%s',_HEAD='%s',_MSG='%s',_TYPE=%d where _DATE='%s';", dateFormat.format(now), head_value, body_value, style_value, ortime);
                    try {
                        db.execSQL(sql);
                        Toast.makeText(MatterDetailsActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MatterDetailsActivity.this, MemoBookActivity.class);
                        startActivity(intent);
                        MatterDetailsActivity.this.finish();
                    } catch (Exception e) {
                        Toast.makeText(MatterDetailsActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                    }
                    Intent intent = new Intent(MatterDetailsActivity.this, MemoBookActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });


    }
}
