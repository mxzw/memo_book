package com.bhp.memo_book.ui.register.data;

import com.bhp.memo_book.ui.register.data.model.RegisterInUser;
import com.bhp.memo_book.utils.AppProxy;
import com.bhp.memo_book.utils.DatabaseHelper;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class RegisterDataSource {

    public Result<RegisterInUser> register(String phone, String password, String userName, String email) {

        try {
            // TODO: handle loggedInUser authentication
            long res = DatabaseHelper.getInstance(AppProxy.getInstance().getContext()).insertUserInfo(phone, password, userName, email);

            return res != -1 ? new Result.Success<>(new RegisterInUser(String.valueOf(res), userName)) :
                    new Result.Error(new IOException("Error register: uid is -1"));
        } catch (Exception e) {
            return new Result.Error(new IOException("Error register in", e));
        }
    }

}