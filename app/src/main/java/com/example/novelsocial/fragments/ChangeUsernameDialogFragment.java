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

import com.example.novelsocial.databinding.FragmentChangeUsernameDialogBinding;
import com.example.novelsocial.interfaces.DialogListener;

import java.util.Objects;

public class ChangeUsernameDialogFragment extends DialogFragment {

    FragmentChangeUsernameDialogBinding binding;
    Button updateUsernameButton;
    EditText newUsernameField;

    public ChangeUsernameDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChangeUsernameDialogBinding.inflate(getLayoutInflater(), container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get field and button from view
        updateUsernameButton = binding.btUpdateUsername;
        newUsernameField = binding.etNewUsername;

        newUsernameField.requestFocus();
        Objects.requireNonNull(getDialog()).getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        updateUsernameButton.setOnClickListener(new View.OnClickListener() {
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
            listener.onFinishChangeUsername(newUsernameField.getText().toString());
        }
        // Closes the dialog fragment
        dismiss();
    }
}