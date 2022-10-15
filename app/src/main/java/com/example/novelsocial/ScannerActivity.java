package com.example.novelsocial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
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
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.novelsocial.databinding.ActivityScannerBinding;
import com.example.novelsocial.models.Book;
import com.example.novelsocial.utils.QRCodeFoundListener;
import com.google.common.util.concurrent.ListenableFuture;

import org.parceler.Parcels;

import java.util.concurrent.ExecutionException;

public class ScannerActivity extends AppCompatActivity {

    public static final int PERMISSION_REQUEST_CAMERA = 0;
    private ListenableFuture<ProcessCameraProvider> cameraProviderListenableFuture;
    private PreviewView previewView;
    private Button qrCodeFound;
    private String qrCode;
    private Book bookObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.novelsocial.databinding.ActivityScannerBinding binding = ActivityScannerBinding.inflate(getLayoutInflater());

        View view = binding.getRoot();
        setContentView(view);

        previewView = binding.qrcodePreview;
        qrCodeFound = binding.btScanQrCode;

        // Make button invisible when no QR Code is found
        qrCodeFound.setVisibility(View.INVISIBLE);

        qrCodeFound.setOnClickListener(v -> {
            Toast.makeText(ScannerActivity.this.getApplicationContext(), qrCode, Toast.LENGTH_SHORT).show();
            Log.i(ScannerActivity.class.getSimpleName(), "QR Code Found: " + qrCode);

//            // Get Book Information using the QR Code
//            getBookObject();
//
//            Intent intent = new Intent(this, BookDetailsActivity.class);
//            intent.putExtra("BookObject", Parcels.wrap(bookObject));
//            startActivity(intent);
//            finish();
        });
        
        cameraProviderListenableFuture = ProcessCameraProvider.getInstance(ScannerActivity.this.getApplicationContext());
        requestCamera();
    }

    private void getBookObject() {

    }


    private void requestCamera() {
        if (ActivityCompat.checkSelfPermission(ScannerActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(ScannerActivity.this, Manifest.permission.CAMERA)) {
                ActivityCompat.requestPermissions(ScannerActivity.this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
            } else {
                ActivityCompat.requestPermissions(ScannerActivity.this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
            }
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
        // previewView.setPreferredImplementationMode(PreviewView.ImplementationMode.SURFACE_VIEW);
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

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this), new QRCodeAnalyzer(new QRCodeFoundListener() {
            @Override
            public void onQRCodeFound(String _qrCode) {
                qrCode = _qrCode;
                qrCodeFound.setVisibility(View.VISIBLE);
            }

            @Override
            public void qrCodeNotFound() {
                qrCodeFound.setVisibility(View.INVISIBLE);
            }
        }));
        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, imageAnalysis, preview);
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