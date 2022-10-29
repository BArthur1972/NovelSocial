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

import com.example.novelsocial.databinding.FragmentChangeFullNameDialogBinding;
import com.example.novelsocial.interfaces.DialogListener;

import java.util.Objects;

public class ChangeFullNameDialogFragment extends DialogFragment {

    FragmentChangeFullNameDialogBinding binding;
    EditText fullNameField;
    Button updateFullNameButton;

    public ChangeFullNameDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChangeFullNameDialogBinding.inflate(getLayoutInflater(), container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get field and button from view
        updateFullNameButton = binding.btUpdateFullName;
        fullNameField = binding.etFullName;

        fullNameField.requestFocus();
        Objects.requireNonNull(getDialog()).getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        updateFullNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendBackResult();
            }
        });
    }

    // Call this method to send the data back to the parent fragment
    public void sendBackResult() {
        // `getTargetFragment` will be set when the dialog is displayed
        DialogListener listener = (DialogListener) getTargetFragment();
        if (listener != null) {
            listener.onFinishChangeFullNameDialog(fullNameField.getText().toString());
        }
        // Closes the dialog fragment
        dismiss();
    }
}