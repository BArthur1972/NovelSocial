package com.example.novelsocial;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.novelsocial.models.Book;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.Locale;
import java.util.Objects;

public class BookDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        // Add the back button in the ActionBar to go back a previous page
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // Find all components we want to populate
        ImageView bookImageCover = findViewById(R.id.iv_book_cover_view);
        TextView bookTitle = findViewById(R.id.tv_book_title);
        TextView bookAuthor = findViewById(R.id.tv_author_name);
        TextView bookPublisher = findViewById(R.id.tv_publisher_name);
        TextView bookGenre = findViewById(R.id.tv_genre);
        TextView bookPages = findViewById(R.id.tv_no_of_pages);

        // Retrieve book data from Book Object in the bundle
        Book book = (Book) Parcels.unwrap(getIntent().getParcelableExtra("BookObject"));

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
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Navigate back to search activity when back button is pressed
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return false;
    }
}