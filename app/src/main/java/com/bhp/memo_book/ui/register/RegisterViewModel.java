package com.bhp.memo_book.ui.register;

import android.util.Patterns;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bhp.memo_book.R;
import com.bhp.memo_book.ui.register.data.RegisterRepository;
import com.bhp.memo_book.ui.register.data.Result;
import com.bhp.memo_book.ui.register.data.model.RegisterInUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterViewModel extends ViewModel {

    private MutableLiveData<Boolean> loginFormState = new MutableLiveData<>();
    private MutableLiveData<RegisterResult> loginResult = new MutableLiveData<>();
    private RegisterRepository registerRepository;

    RegisterViewModel(RegisterRepository registerRepository) {
        this.registerRepository = registerRepository;
    }

    LiveData<Boolean> getLoginFormState() {
        return loginFormState;
    }

    LiveData<RegisterResult> getLoginResult() {
        return loginResult;
    }

    public void register(String phone, String password, String userName, String email) {
        // can be launched in a separate asynchronous job
        Result<RegisterInUser> result = registerRepository.register(phone, password, userName, email);

        if (result instanceof Result.Success) {
            RegisterInUser data = ((Result.Success<RegisterInUser>) result).getData();
            loginResult.setValue(new RegisterResult(new RegisterInUserView(data.getUserId(),data.getDisplayName())));
        } else {
            loginResult.setValue(new RegisterResult(R.string.register_failed));
        }
    }

    public void registerDataChanged(String phone, String password, String username, String email) {

        if (!isUserPhoneValid(phone) || !isPasswordValid(password) || !isUserNameValid(username) || !isUserEmailValid(email)) {
            loginFormState.setValue(false);
        } else {
            loginFormState.setValue(true);
        }

    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        return !username.trim().isEmpty();
    }

    private boolean isUserPhoneValid(String phone) {
        if (phone == null) {
            return false;
        }
        return phone != null && phone.trim().length() <= 11;

    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    private boolean isUserEmailValid(String email) {
        if (email == null) {
            return false;
        }

        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}