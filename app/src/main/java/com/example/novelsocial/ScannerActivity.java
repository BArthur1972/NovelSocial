package com.example.novelsocial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.novelsocial.clients.ScannedBookClient;
import com.example.novelsocial.databinding.ActivityScannerBinding;
import com.example.novelsocial.models.Book;
import com.example.novelsocial.interfaces.BarcodeFoundListener;
import com.google.common.util.concurrent.ListenableFuture;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

import okhttp3.Headers;

public class ScannerActivity extends AppCompatActivity {

    public static final int PERMISSION_REQUEST_CAMERA = 0;
    private ListenableFuture<ProcessCameraProvider> cameraProviderListenableFuture;
    private PreviewView previewView;
    private Button barcodeFoundBtn;
    private String barcode;
    private Book bookObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityScannerBinding binding = ActivityScannerBinding.inflate(getLayoutInflater());

        View view = binding.getRoot();
        setContentView(view);

        // Add the back button in the ActionBar to go back a previous page
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        previewView = binding.barcodePreview;
        barcodeFoundBtn = binding.btScanBarcode;

        // Make button invisible when no QR Code is found
        barcodeFoundBtn.setVisibility(View.INVISIBLE);

        barcodeFoundBtn.setOnClickListener(v -> {
            Toast.makeText(ScannerActivity.this.getApplicationContext(), barcode, Toast.LENGTH_SHORT).show();
            Log.i(ScannerActivity.class.getSimpleName(), "ISBN Found: " + barcode);

            // Get Book Information using the QR Code and navigate to the
            // BookDetailsActivity with the Book object found as an Intent extra
            getBookObject(barcode);
        });

        cameraProviderListenableFuture = ProcessCameraProvider.getInstance(ScannerActivity.this.getApplicationContext());
        requestCamera();
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

    public void getBookObject(String code) {
        // Create ScannedBookClient object and call the getBooks method which makes the API call
        ScannedBookClient scannedBookClient= new ScannedBookClient();
        scannedBookClient.getBooks(code, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject jsonResponse = json.jsonObject;
                try {
                    JSONObject body = jsonResponse.getJSONObject("ISBN:" + code);
                    bookObject = Book.fromJsonWithScannedISBN(body);
                    goToBookDetailsActivity(bookObject);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.i(ScannerActivity.class.getSimpleName(), "Request failed with code: " + statusCode + " , Response message: " + response);
            }
        });
    }

    private void goToBookDetailsActivity(Book bookObject){
        Intent intent = new Intent(this, BookDetailsActivity.class);
        intent.putExtra("BookObject", Parcels.wrap(bookObject));
        startActivity(intent);
        finish();
    }

    private void requestCamera() {
        if (ActivityCompat.checkSelfPermission(ScannerActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(ScannerActivity.this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
        }
    }

    private void startCamera() {
        cameraProviderListenableFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderListenableFuture.get();
                bindCameraToPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                Toast.makeText(this, "Failed to start camera" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void bindCameraToPreview(ProcessCameraProvider cameraProvider) {
        previewView.setPreferredImplementationMode(PreviewView.ImplementationMode.SURFACE_VIEW);

        Preview preview = new Preview.Builder()
                .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        preview.setSurfaceProvider(previewView.createSurfaceProvider());

        ImageAnalysis imageAnalysis =
                new ImageAnalysis.Builder()
                        .setTargetResolution(new Size(1280, 720))
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this), new BarcodeAnalyzer(new BarcodeFoundListener() {
            @Override
            public void barcodeFound(String _qrCode) {
                barcode = _qrCode;
                barcodeFoundBtn.setVisibility(View.VISIBLE);
            }

            @Override
            public void barcodeNotFound() {
                barcodeFoundBtn.setVisibility(View.INVISIBLE);
            }
        }));

        cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, imageAnalysis, preview);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            } else {
                Toast.makeText(this, "Permission to use the has been camera denied", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}