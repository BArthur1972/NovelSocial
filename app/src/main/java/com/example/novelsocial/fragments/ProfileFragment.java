package com.example.novelsocial.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.novelsocial.LoginActivity;
import com.example.novelsocial.databinding.FragmentProfileBinding;
import com.example.novelsocial.interfaces.DialogListener;
import com.example.novelsocial.models.LibraryItem;
import com.parse.DeleteCallback;
import com.parse.GetDataCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Objects;

public class ProfileFragment extends Fragment implements DialogListener {

    FragmentProfileBinding binding;
    ActivityResultLauncher<Intent> imagePickerActivityResult;
    ImageView profileImageView;
    TextView firstAndLastName;
    TextView username;
    TextView numberOfLibraryItems;


    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(getLayoutInflater(), container, false);

        // Register ActivityResult to replace placeholder image with image from gallery
        imagePickerActivityResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getData() != null) {
                            Uri imageUri = result.getData().getData();
                            // Save new photo to parse
                            try {
                                saveImageToParse(imageUri);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            // Replace old photo with new photo
                            Glide.with(requireContext())
                                    .load(imageUri)
                                    .into(profileImageView);
                        }
                    }
                }
        );

        // layout of fragment is stored in a special property called root
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Bind Views
        ListView accountOptionsListView = binding.lvAccountOptionsList;
        profileImageView = binding.ivUserProfilePhoto;
        firstAndLastName = binding.tvUserProfileName;
        username = binding.tvProfileUsername;
        numberOfLibraryItems = binding.tvNoOfLibraryItems;

        ParseUser user = ParseUser.getCurrentUser();

        firstAndLastName.setText(Objects.requireNonNull(user.get("fullName")).toString());

        username.setText(user.getUsername());

        int libraryBooks = getNumberOfLibraryItems();
        String formatString = "You Have No Books";
        if (libraryBooks == 1) {
            formatString = "You Have %d Book";
        } else if (libraryBooks > 1) {
            formatString = "You Have %d Books";
        }
        numberOfLibraryItems.setText(String.format(Locale.ENGLISH,formatString, libraryBooks));

        // Populate Account Options List and Set an Adapter on the Array.
        String[] accountOptions = new String[]{"Logout", "Update Your Full Name", "Update Your Username", "Update your Password", "Change Profile Photo", "Delete Your Account"};

        accountOptionsListView.setNestedScrollingEnabled(true);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, accountOptions);
        accountOptionsListView.setAdapter(adapter);

        accountOptionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Handle each account options case
                switch (i) {
                    case (0): {
                        logOut();
                        Toast.makeText(getContext(), "You Have Been Logged Out", Toast.LENGTH_SHORT).show();
                        break;
                    }

                    // Update full name
                    case 1: {
                        showChangeFullNameDialogFragment();
                        break;
                    }

                    // Update username
                    case 2: {
                        showChangeUsernameDialogFragment();
                        break;
                    }

                    // Update password
                    case 3: {
                        showChangePasswordDialogFragment();
                        break;
                    }

                    case 4: {
                        updateProfilePhoto(imagePickerActivityResult);
                        break;
                    }

                    // Delete account
                    case 5: {
                        deleteAccount();
                    }
                }
            }
        });

        // Load profile photo from parse and display it
        loadImageFromParse();
    }

    private void logOut() {
        ParseUser.logOutInBackground(new LogOutCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    goToLoginActivity();
                } else {
                    Toast.makeText(getContext(), "Failed To Logout", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void showChangeFullNameDialogFragment() {
        FragmentManager fragmentManager = getParentFragmentManager();
        ChangeFullNameDialogFragment fragment = new ChangeFullNameDialogFragment();

        // SETS the target fragment for use later when sending results
        fragment.setTargetFragment(ProfileFragment.this, 300);
        fragment.show(fragmentManager, "change_full_name");
    }

    private void showChangeUsernameDialogFragment() {
        FragmentManager fragmentManager = getParentFragmentManager();
        ChangeUsernameDialogFragment fragment = new ChangeUsernameDialogFragment();

        // SETS the target fragment for use later when sending results
        fragment.setTargetFragment(ProfileFragment.this, 300);
        fragment.show(fragmentManager, "change_username");
    }

    private void showChangePasswordDialogFragment() {
        FragmentManager fragmentManager = getParentFragmentManager();
        ChangePasswordDialogFragment fragment = new ChangePasswordDialogFragment();

        // SETS the target fragment for use later when sending results
        fragment.setTargetFragment(ProfileFragment.this, 300);
        fragment.show(fragmentManager, "change_password");
    }

    // This is called when the Change full name dialog is completed and the results have been passed
    @Override
    public void onFinishChangeFullNameDialog(String newName) {
        ParseUser user = ParseUser.getCurrentUser();
        user.put("fullName", newName);
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                firstAndLastName.setText(newName);
                Toast.makeText(getContext(), "Name has been updated", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onFinishChangeUsername(String newUsername) {
        ParseUser user = ParseUser.getCurrentUser();
        user.put("username", newUsername);
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                username.setText(newUsername);
                Toast.makeText(getContext(), "Username has been updated", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onFinishChangePassword(String newPassword, String confirmPassword) {
        ParseUser user = ParseUser.getCurrentUser();
        if (!Objects.equals(newPassword, confirmPassword)) {
            Toast.makeText(getContext(), "Passwords do no match, Try Again", Toast.LENGTH_SHORT).show();
            showChangePasswordDialogFragment();
        } else {
            user.setPassword(newPassword);
            user.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    Toast.makeText(getContext(), "Your Password has been updated", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public int getNumberOfLibraryItems() {
        ParseUser user = ParseUser.getCurrentUser();

        int numberOfBooksInLibrary = 0;
        ParseQuery<LibraryItem> query = ParseQuery.getQuery("LibraryItem");

        // Filter the query for books belonging to the current user
        query.whereContains("owner", user.getObjectId());

        //Fetches count synchronously,this will block the main thread
        try {
            numberOfBooksInLibrary = query.count();
        } catch (ParseException e) {
            Log.e(ProfileFragment.class.getSimpleName(), "Error fetching number of book in library: " + e.getMessage());
        }

        return numberOfBooksInLibrary;
    }


    public void loadImageFromParse() {
        ParseUser user = ParseUser.getCurrentUser();

        ParseFile profilePhoto = user.getParseFile("profilePhoto");
        if (profilePhoto != null) {
            profilePhoto.getDataInBackground(new GetDataCallback() {
                public void done(byte[] data, ParseException e) {
                    if (e == null) {
                        // data has the bytes for the image
                        Bitmap img = BitmapFactory
                                .decodeByteArray(
                                        data, 0,
                                        data.length);

                        Glide.with(requireContext())
                                .load(img)
                                .into(profileImageView);

                    } else {
                        Log.e(ProfileFragment.class.getSimpleName(), "Problem Loading Image");
                    }
                }
            });
        }
    }

    public void saveImageToParse(Uri uri) throws IOException {
        InputStream inputStream = requireContext().getContentResolver().openInputStream(uri);
        byte[] inputData = getBytes(inputStream);

        ParseFile file = new ParseFile("user_profile_photo", inputData);
        file.saveInBackground();

        ParseUser user = ParseUser.getCurrentUser();
        user.put("profilePhoto", file);
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Toast.makeText(getContext(), "Saved File Successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public void goToLoginActivity() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        requireActivity().overridePendingTransition(0, 0);
    }

    public void updateProfilePhoto (ActivityResultLauncher<Intent> activityResultLauncher) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        activityResultLauncher.launch(photoPickerIntent);
    }

    public void deleteAccount() {
        ParseUser user = ParseUser.getCurrentUser();

        user.deleteInBackground(new DeleteCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(getContext(), "Account Has Been Deleted", Toast.LENGTH_SHORT).show();
                    logOut();
                }
                else {
                    Toast.makeText(getContext(), "Failed To Delete Your Account", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}