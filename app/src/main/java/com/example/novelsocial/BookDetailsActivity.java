package com.example.novelsocial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.novelsocial.models.Book;
import com.example.novelsocial.models.LibraryItem;

import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.Locale;
import java.util.Objects;

public class BookDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        // Retrieve book data from Book Object in the bundle
        Book book = (Book) Parcels.unwrap(getIntent().getParcelableExtra("BookObject"));

        // Add the back button in the ActionBar to go back a previous page
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // Add book to Library
        Button addBookToLibrary = findViewById(R.id.bt_add_to_library);
        addBookToLibrary.setOnClickListener(v -> {
            ParseUser currentUser = ParseUser.getCurrentUser();
            LibraryItem libraryItem = new LibraryItem(book);

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

        // Navigate to WebView Activity when button is clicked
        Button toWebViewActivity = findViewById(R.id.bt_web_view_activity);

        Log.i(BookDetailsActivity.this.getClass().getSimpleName(), "Book's ISBN " + book.getISBN());

        toWebViewActivity.setOnClickListener(v -> goToWebViewActivity((book.getISBN())));

        // Find all components we want to populate
        ImageView bookImageCover = findViewById(R.id.iv_book_cover_view);
        TextView bookTitle = findViewById(R.id.tv_book_title);
        TextView bookAuthor = findViewById(R.id.tv_author_name);
        TextView bookPublisher = findViewById(R.id.tv_publisher_name);
        TextView bookGenre = findViewById(R.id.tv_genre);
        TextView bookPages = findViewById(R.id.tv_no_of_pages);

        // Set Book Cover in layout and remaining views with their respective values
        Picasso.get()
                .load(book.getBookCoverUrl())
                .placeholder(R.drawable.ic_nocover)
                .into(bookImageCover);

        bookTitle.setText(book.getTitle());
        bookAuthor.setText(book.getAuthor());
        bookPublisher.setText(String.valueOf(book.getPublisher()));
        bookGenre.setText(book.getGenre());
        bookPages.setText(String.format(Locale.ENGLISH, "No. Of Pages: %d", book.getPages()));
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

    public void goToWebViewActivity(String isbn) {
        Intent intent = new Intent(BookDetailsActivity.this, WebViewActivity.class);
        intent.putExtra("BookISBN", isbn);
        startActivity(intent);
    }
}