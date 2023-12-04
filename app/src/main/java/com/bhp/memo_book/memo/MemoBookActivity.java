package com.bhp.memo_book.memo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Time;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bhp.memo_book.R;
import com.bhp.memo_book.databinding.ActivityMemoBookBinding;
import com.bhp.memo_book.ui.BaseActivity;
import com.bhp.memo_book.utils.DatabaseHelper;

import java.util.ArrayList;


public class MemoBookActivity extends BaseActivity {
    private ArrayList<Item> data = new ArrayList<>();
    DatabaseHelper helper;

    private ActivityMemoBookBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_memo_book);

        String username = getIntent().getStringExtra("username");
        String uid = getIntent().getStringExtra("uid");
        //获取用户名
        final SharedPreferences sp = getSharedPreferences("data", Context.MODE_PRIVATE);
        if (TextUtils.isEmpty(username)) {
            username = sp.getString("username", "unknown user");
        }

        if (TextUtils.isEmpty(uid)) {
            uid = sp.getString("uid", "");
        }
//        String name = sp.getString("name", "unknown user");
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("uid", uid);
        edit.putString("username", username);
        edit.commit();

        binding.userName.setText(username + "！");
        //获取当前时间，并搭配欢迎语
        Time t = new Time();
        t.setToNow();
        int hour = t.hour;
        if (hour >= 0 && hour <= 5) {
            binding.timeHour.setText("凌晨好，");
            binding.intro.setText("太阳还没起床，请注意作息噢");
        } else if (hour >= 6 && hour <= 10) {
            binding.timeHour.setText("早上好，");
            binding.intro.setText("一天之计在于晨，多写多记助你成长");
        } else if (hour >= 11 && hour <= 13) {
            binding.timeHour.setText("中午好，");
            binding.intro.setText("中午是健忘时刻，重要事务勿忘记");
        } else if (hour >= 14 && hour <= 19) {
            binding.timeHour.setText("下午好，");
            binding.intro.setText("来杯咖啡，小憩一下，工作效率会更高");
        } else {
            binding.timeHour.setText("晚上好，");
            binding.intro.setText("今日事今日毕，请查看事项是否完成");
        }

        //设置
        binding.setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MemoBookActivity.this, UserDetailsActivity.class);
                startActivity(intent);
            }
        });
        //添加事项
        binding.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MemoBookActivity.this, NewMatterActivity.class);
                startActivity(intent);
            }
        });

        //List加载
        helper = DatabaseHelper.getInstance(this);
        final SQLiteDatabase db = helper.getWritableDatabase();
        String sql = String.format("select _HEAD,_MSG,_DATE,_TYPE from res where _NAME='%s';", username);
        if (username == "") {
            Toast.makeText(MemoBookActivity.this, "获取用户名异常", Toast.LENGTH_SHORT).show();
        } else {
            Cursor cursor = db.rawQuery(sql, null);
            final ItemAdapter adapter = new ItemAdapter(this, R.layout.item_list, data);
            final ListView listView = (ListView) findViewById(R.id.list);

            while (cursor.moveToNext()) {
                Item a = new Item(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3));
                data.add(a);
            }
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String date = (String) ((TextView) view.findViewById(R.id.list_item_time)).getText();
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("time", date);
                    editor.commit();
                    Intent intent = new Intent(MemoBookActivity.this, MatterDetailsActivity.class);
                    startActivity(intent);
//                    MemoBookActivity.this.finish();
                }
            });

            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String date = (String) ((TextView) view.findViewById(R.id.list_item_time)).getText();
                    String del = String.format("delete from res where _DATE='%s';", date);
                    try {
                        db.execSQL(del);
                        Toast.makeText(MemoBookActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                        //刷新页面
                        Intent intent = new Intent(MemoBookActivity.this, MemoBookActivity.class);
                        startActivity(intent);
                        MemoBookActivity.this.finish();

                    } catch (Exception e) {
                        Toast.makeText(MemoBookActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
            });

        }

    }


}
