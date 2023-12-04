package com.bhp.memo_book.ui.register.data;

import com.bhp.memo_book.ui.register.data.model.RegisterInUser;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class RegisterRepository {

    private static volatile RegisterRepository instance;

    private RegisterDataSource dataSource;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private RegisterInUser user = null;

    // private constructor : singleton access
    private RegisterRepository(RegisterDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static RegisterRepository getInstance(RegisterDataSource dataSource) {
        if (instance == null) {
            instance = new RegisterRepository(dataSource);
        }
        return instance;
    }

    public Result<RegisterInUser> register(String phoneNum, String password, String userName, String email) {
        // handle login
        Result<RegisterInUser> result = dataSource.register(phoneNum, password, userName, email);
        return result;
    }
}