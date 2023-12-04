package com.bhp.memo_book.ui.login;

import android.app.Activity;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bhp.memo_book.R;
import com.bhp.memo_book.memo.MemoBookActivity;
import com.bhp.memo_book.ui.BaseActivity;
import com.bhp.memo_book.ui.login.LoginViewModel;
import com.bhp.memo_book.ui.login.LoginViewModelFactory;
import com.bhp.memo_book.databinding.ActivityLoginBinding;
import com.bhp.memo_book.utils.UtilsHelper;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends BaseActivity {

    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);


        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);


        loginViewModel.getLoginFormState().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                binding.btnSignIn.setEnabled(loginFormState);
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(binding.etPhone.getText().toString(),
                        binding.etPassword.getText().toString());
            }
        };
        binding.etPhone.addTextChangedListener(afterTextChangedListener);
        binding.etPassword.addTextChangedListener(afterTextChangedListener);

        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginViewModel.login(binding.etPhone.getText().toString(),
                        binding.etPassword.getText().toString());
            }
        });
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MemoBookActivity.class);
        intent.putExtra("uid", model.getUid());
        intent.putExtra("username", model.getDisplayName());
        startActivity(intent);

    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }


}