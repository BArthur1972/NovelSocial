package com.example.novelsocial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.novelsocial.databinding.ActivityWebViewBinding;

import java.util.Objects;

public class WebViewActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       ActivityWebViewBinding binding = ActivityWebViewBinding.inflate(getLayoutInflater());

        View root = binding.getRoot();
        setContentView(root);

        // Add the back button in the ActionBar to go back a previous page
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        configureWebView();

        // Get book ISBN or OpenLibraryId for url
        Bundle bundle = getIntent().getExtras();
        String[] bookData = bundle.getStringArray("BookData");
        String url = (bookData[0] == null) ? "https://openlibrary.org/books/" + bookData[1] : "https://openlibrary.org/isbn/" + bookData[0];

        webView.loadUrl(url);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }
        });
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

    private void configureWebView() {
        webView = findViewById(R.id.web_view);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        WebView.setWebContentsDebuggingEnabled(false);
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.getSettings().setAppCacheEnabled(true);
        webView.setVerticalFadingEdgeEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        settings.setDomStorageEnabled(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        settings.setUseWideViewPort(true);
        settings.setSavePassword(true);
        settings.setSaveFormData(true);
        settings.setEnableSmoothTransition(true);
    }
}