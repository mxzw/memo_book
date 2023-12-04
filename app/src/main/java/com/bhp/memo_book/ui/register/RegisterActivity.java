package com.bhp.memo_book.ui.register;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bhp.memo_book.R;
import com.bhp.memo_book.databinding.ActivityRegisterBinding;
import com.bhp.memo_book.memo.MemoBookActivity;
import com.bhp.memo_book.ui.BaseActivity;


public class RegisterActivity extends BaseActivity {

    private RegisterViewModel registerViewModel;
    private ActivityRegisterBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);


        registerViewModel = new ViewModelProvider(this, new RegisterViewModelFactory())
                .get(RegisterViewModel.class);


        registerViewModel.getLoginResult().observe(this, new Observer<RegisterResult>() {
            @Override
            public void onChanged(@Nullable RegisterResult registerResult) {
                if (registerResult == null) {
                    return;
                }
                if (registerResult.getError() != null) {
                    showLoginFailed(registerResult.getError());
                }
                if (registerResult.getSuccess() != null) {
                    updateUiWithUser(registerResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                finish();
            }
        });

        registerViewModel.getLoginFormState().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                binding.btnSignUp.setEnabled(aBoolean);
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
                registerViewModel.registerDataChanged(binding.etRegisterPhone.getText().toString(),
                        binding.etRegisterPassword.getText().toString(),
                        binding.etRegisterUsername.getText().toString(),
                        binding.etRegisterEmail.getText().toString());
            }
        };
        binding.etRegisterPhone.addTextChangedListener(afterTextChangedListener);
        binding.etRegisterPassword.addTextChangedListener(afterTextChangedListener);
        binding.etRegisterUsername.addTextChangedListener(afterTextChangedListener);
        binding.etRegisterEmail.addTextChangedListener(afterTextChangedListener);

        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerViewModel.register(binding.etRegisterPhone.getText().toString(),
                        binding.etRegisterPassword.getText().toString(),
                        binding.etRegisterUsername.getText().toString(),
                        binding.etRegisterEmail.getText().toString());
            }
        });
    }

    private void updateUiWithUser(RegisterInUserView model) {
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