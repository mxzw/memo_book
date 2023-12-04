package com.bhp.memo_book;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Launcher);
        initCountdown();
    }

    private void initCountdown() {
        CountDownTimer mTimer = new CountDownTimer(1000, 500) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                /*skipBtn.setVisibility(View.VISIBLE);
                skipBtn.setText((millisUntilFinished / 1000 + 1) + "s 跳过");*/
            }

            @Override
            public void onFinish() {
                // 通知Main 结束闪屏
                gotoNext();
            }
        }.start();
    }

    private void gotoNext() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


}