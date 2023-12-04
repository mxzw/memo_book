package com.bhp.memo_book.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;

import com.bhp.memo_book.ui.login.data.LoginRepository;
import com.bhp.memo_book.ui.login.data.Result;
import com.bhp.memo_book.ui.login.data.model.LoggedInUser;
import com.bhp.memo_book.R;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<Boolean> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<Boolean> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String phone, String password) {
        // can be launched in a separate asynchronous job
        Result<LoggedInUser> result = loginRepository.login(phone, password);

        if (result instanceof Result.Success) {
            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getUserId(), data.getDisplayName())));
        } else {
            loginResult.setValue(new LoginResult(R.string.login_failed));
        }
    }

    public void loginDataChanged(String phone, String password) {
        if (!isUserNameValid(phone) || !isPasswordValid(password)) {
            loginFormState.setValue(false);
        } else {
            loginFormState.setValue(true);
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String phone) {
        return phone != null && phone.trim().length() <= 11;

    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}