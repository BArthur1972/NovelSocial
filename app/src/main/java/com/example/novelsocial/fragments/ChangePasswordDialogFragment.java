package com.example.novelsocial.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.novelsocial.databinding.FragmentChangePasswordDialogBinding;
import com.example.novelsocial.interfaces.DialogListener;

import java.util.Objects;

public class ChangePasswordDialogFragment extends DialogFragment {

    FragmentChangePasswordDialogBinding binding;
    Button updatePasswordButton;
    EditText newPasswordField;
    EditText confirmPasswordField;

    public ChangePasswordDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChangePasswordDialogBinding.inflate(getLayoutInflater(), container,false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get field and button from view
        updatePasswordButton = binding.btUpdatePassword;
        newPasswordField = binding.etNewPasswordField;
        confirmPasswordField = binding.etConfirmNewPasswordField;

        newPasswordField.requestFocus();
        Objects.requireNonNull(getDialog()).getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        updatePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendBackResult();
            }
        });
    }

    private void sendBackResult() {
        // `getTargetFragment` will be set when the dialog is displayed
        DialogListener listener = (DialogListener) getTargetFragment();
        if (listener != null) {
            listener.onFinishChangePassword(
                    newPasswordField.getText().toString(),
                    confirmPasswordField.getText().toString()
            );
        }
        // Closes the dialog fragment
        dismiss();
    }
}