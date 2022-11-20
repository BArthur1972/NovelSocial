package com.example.novelsocial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.novelsocial.databinding.ActivityBookDetailsBinding;
import com.example.novelsocial.models.Book;
import com.example.novelsocial.models.LibraryItem;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class BookDetailsActivity extends AppCompatActivity {

    ActivityBookDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityBookDetailsBinding.inflate(getLayoutInflater());

        // Root of the layout
        View view = binding.getRoot();
        setContentView(view);

        // Retrieve book data from Book Object in the bundle
        Book book = Parcels.unwrap(getIntent().getParcelableExtra("BookObject"));

        // Add the back button in the ActionBar to go back a previous page
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // Add book to Library if it is has not already been added
        // The button will only show if the book has not been added
        if (book != null) {
            displayFavouriteButton(book);

            Button addBookToLibrary = binding.btAddToLibrary;
            addBookToLibrary.setOnClickListener(v -> {
                ParseUser currentUser = ParseUser.getCurrentUser();
                LibraryItem libraryItem = new LibraryItem(book);

                // Set item to the current user
                libraryItem.setOwner(currentUser);

                libraryItem.saveInBackground(e -> {
                    if (e != null) {
                        Toast.makeText(BookDetailsActivity.this, "Failed to add book to Library", Toast.LENGTH_SHORT).show();
                        Log.e(getClass().getSimpleName(), "Failed to add!");
                        return;
                    }
                    currentUser.add("LibraryItem", libraryItem);
                    currentUser.saveInBackground();
                    Toast.makeText(BookDetailsActivity.this, "The Book has been added to your Library", Toast.LENGTH_SHORT).show();
                });
            });

            // Find all components we want to populate
            ImageView bookImageCover = binding.ivBookCoverView;
            TextView bookTitle = binding.tvBookTitle;
            TextView bookAuthor = binding.tvAuthorName;
            TextView bookPublisher = binding.tvPublisherName;
            TextView bookGenre = binding.tvGenre;
            TextView bookPages = binding.tvNoOfPages;

            // Set Book Cover in layout and remaining views with their respective values
            Picasso.get()
                    .load(book.getBookCoverUrl())
                    .placeholder(R.drawable.ic_nocover)
                    .into(bookImageCover);

            bookTitle.setText(book.getTitle());
            bookAuthor.setText(String.format("Author: %s", book.getAuthor()));
            bookPublisher.setText(String.format("Publisher: %s", book.getPublisher()));
            bookGenre.setText(String.format("Genre: %s", book.getGenre()));
            bookPages.setText(String.format(Locale.ENGLISH, "No. Of Pages: %d", book.getPages()));

            // Navigate to WebView Activity when button is clicked
            Button toWebViewActivity = binding.btWebViewActivity;

            toWebViewActivity.setOnClickListener(v -> goToWebViewActivity((book.getISBN()), book.getOpenLibraryId()));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_book_details, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Navigate back to search activity when back button is pressed
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void displayFavouriteButton(Book book) {
        ParseQuery<LibraryItem> query = new ParseQuery<>(LibraryItem.class);
        query.whereEqualTo("bookId", String.valueOf(book.getOpenLibraryId()));
        query.findInBackground(new FindCallback<LibraryItem>() {
            @Override
            public void done(List<LibraryItem> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() == 0) {
                        binding.btAddToLibrary.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    public void goToWebViewActivity(String isbn, String openLibraryId) {
        Intent intent = new Intent(BookDetailsActivity.this, WebViewActivity.class);
        intent.putExtra("BookData", new String[]{isbn, openLibraryId});
        startActivity(intent);
    }
}