package com.example.novelsocial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.novelsocial.Adapters.CommentAdapter;
import com.example.novelsocial.databinding.ActivityBookDetailsBinding;
import com.example.novelsocial.models.Book;
import com.example.novelsocial.models.BookComments;
import com.example.novelsocial.models.LibraryItem;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class BookDetailsActivity extends AppCompatActivity {

    ActivityBookDetailsBinding binding;
    List<BookComments> bookCommentsList;
    CommentAdapter commentsAdapter;

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

        // Set up adapter and recycler view for the books comments
        bookCommentsList = new ArrayList<>();
        commentsAdapter = new CommentAdapter(this, bookCommentsList);
        binding.rvComments.setAdapter(commentsAdapter);
        binding.rvComments.setLayoutManager(new LinearLayoutManager(this));
        binding.ibToggleComments.setTag("opened");
        
        // Hide/Show comments
        binding.ibToggleComments.setOnClickListener(v -> {
            String resource = (String) binding.ibToggleComments.getTag();
            if(Objects.equals(resource, "opened")){
                binding.rvComments.setVisibility(View.GONE);
                binding.etCommentBox.setVisibility(View.GONE);
                binding.btAddComment.setVisibility(View.GONE);
                binding.ibToggleComments.setTag("closed");
                binding.ibToggleComments.setImageResource(R.drawable.ic_baseline_arrow_drop_up_24);
            }
            else{
                binding.rvComments.setVisibility(View.VISIBLE);
                binding.etCommentBox.setVisibility(View.VISIBLE);
                binding.btAddComment.setVisibility(View.VISIBLE);
                binding.ibToggleComments.setTag("opened");
                binding.ibToggleComments.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24);
            }

        });

        // Allow users to add comments
        Button addCommentButton = binding.btAddComment;
        addCommentButton.setOnClickListener(v -> {
            BookComments bookComment = new BookComments();
            bookComment.setComment(binding.etCommentBox.getText().toString());
            bookComment.setBookId(book.getOpenLibraryId());
            bookComment.setUser(ParseUser.getCurrentUser());
            bookComment.saveInBackground();
            bookCommentsList.add(bookComment);
            commentsAdapter.notifyDataSetChanged();
            binding.etCommentBox.setText("");
        });

        // Add book to Library if it has not already been added
        // The button will only show if the book has not been added
        if (book != null) {
            displayFavouriteButton(book);

            queryComments(book.getOpenLibraryId());

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

            // Find all remaining components we want to populate
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Navigate back to search activity when back button is pressed
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void queryComments(String bookId) {
        ParseQuery<BookComments> query = ParseQuery.getQuery(BookComments.class);
        query.whereEqualTo(BookComments.KEY_BOOK_ID, bookId);
        query.findInBackground(new FindCallback<BookComments>() {
            @Override
            public void done(List<BookComments> objects, ParseException e) {
                if (e != null) {
                    return;
                }
                bookCommentsList.addAll(objects);
                commentsAdapter.notifyDataSetChanged();
            }
        });
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