package com.bhp.memo_book.memo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.bhp.memo_book.databinding.ActivitySettingBinding;
import com.bhp.memo_book.ui.BaseActivity;
import com.bhp.memo_book.ui.login.LoginActivity;
import com.bhp.memo_book.utils.AppProxy;
import com.bhp.memo_book.utils.DatabaseHelper;

import java.util.ArrayList;

public class UserDetailsActivity extends BaseActivity {
    String pswd_value, sign_value;
    int gender_value;
    String[] gender_option = {"保密", "男", "女"};

    private ActivitySettingBinding binding;

    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting);
        SharedPreferences sp = getSharedPreferences("data", Context.MODE_PRIVATE);
        uid = sp.getString("uid", "-1");

        final ArrayAdapter<String> genderAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, gender_option);
        binding.gender.setAdapter(genderAdapter);

        SQLiteDatabase readableDatabase = DatabaseHelper.getInstance(AppProxy.getInstance().getContext()).getReadableDatabase();
        String sql = String.format("select phone, username, password, email,gender,sign from users where id='%s'", uid);
        Cursor cursor = readableDatabase.rawQuery(sql, null);

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                binding.phone.setText(cursor.getString(0));
                binding.name.setText(cursor.getString(1));
                binding.pswd.setText(cursor.getString(2));
                binding.email.setText(cursor.getString(3));
                gender_value = cursor.getInt(4);
                binding.gender.setSelection(gender_value < 0 || gender_value > 2 ? 0 : gender_value, true);
                sign_value = cursor.getString(5);
                binding.sign.setText(TextUtils.isEmpty(sign_value) ? "这个人很懒，啥也不想说" : sign_value);
            }
        }

        //返回上一级
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserDetailsActivity.this, MemoBookActivity.class);
                startActivity(intent);
                UserDetailsActivity.this.finish();
            }
        });
        //返回登录页
        binding.quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserDetailsActivity.this, LoginActivity.class);
                startActivity(intent);
                UserDetailsActivity.this.finish();
            }
        });

        //提交修改
        binding.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sign_value = binding.sign.getText().toString();
                pswd_value = binding.pswd.getText().toString();
                String choice = binding.gender.getSelectedItem().toString();
                switch (choice) {
                    case "男":
                        gender_value = 1;
                        break;
                    case "女":
                        gender_value = 2;
                        break;
                    default:
                        gender_value = 0;
                        break;
                }
                SQLiteDatabase writableDatabase = DatabaseHelper.getInstance(UserDetailsActivity.this).getWritableDatabase();
                String updateUserInfo = String.format("update users set phone='%s',username='%s',email='%s',sign='%s',gender=%d where id='%s';",
                        binding.phone.getText(), binding.name.getText(), binding.email.getText(), sign_value, gender_value, uid);
                try {
                    writableDatabase.execSQL(updateUserInfo);
                    Toast.makeText(UserDetailsActivity.this, "成功", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(UserDetailsActivity.this, "失败", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(UserDetailsActivity.this, MemoBookActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
