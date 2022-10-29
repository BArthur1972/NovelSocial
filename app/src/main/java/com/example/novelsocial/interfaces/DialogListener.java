package com.example.novelsocial.interfaces;

public interface DialogListener {

    void onFinishChangeFullNameDialog(String newName);

    void onFinishChangeUsername(String newUsername);

    void onFinishChangePassword(String newPassword, String confirmPassword);
}
