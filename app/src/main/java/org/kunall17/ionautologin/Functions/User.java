package org.kunall17.ionautologin.Functions;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kunall17 on 12/24/15.
 */
public class User {
    private String username;
    private String password;
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
