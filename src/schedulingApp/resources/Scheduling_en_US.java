package schedulingApp.resources;

import java.util.ListResourceBundle;

public class Scheduling_en_US extends ListResourceBundle {
    protected Object[][] getContents() {
        return new Object[][] {
                {"intro", "EN Default locale selected"},
                {"openConnection", "Connection open"},
                {"userName", "username"},
                {"password", "password"},
                {"signIn", "Sign In"},
                {"match", "Username/password match!"},
                {"signInButton", "Sign In"},
                {"schedulingApp", "The Scheduling App"},
                {"loginSuccess", "Successfully logged in..."},
                {"loginSuccessDescription", "Username and password match found."},
                {"invalidCredentials", "Invalid credentials"}
        };
    }
}
