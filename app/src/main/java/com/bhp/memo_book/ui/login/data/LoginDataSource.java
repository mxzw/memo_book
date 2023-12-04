package com.bhp.memo_book.ui.login.data;

import com.bhp.memo_book.ui.login.data.model.LoggedInUser;
import com.bhp.memo_book.ui.register.data.model.RegisterInUser;
import com.bhp.memo_book.utils.AppProxy;
import com.bhp.memo_book.utils.DatabaseHelper;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String phone, String password) {

        try {
            DatabaseHelper instance = DatabaseHelper.getInstance(AppProxy.getInstance().getContext());
            long res = instance.validateLogin(phone, password);
            String userName = instance.getColumnUsername(res);
            return res != -1 ? new Result.Success<>(new LoggedInUser(String.valueOf(res), userName)) :
                    new Result.Error(new IOException("Error logging: uid is -1"));
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}