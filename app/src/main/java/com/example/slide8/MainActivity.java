package com.example.slide8;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.core.UseCase;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private ImageView photo;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        photo = findViewById(R.id.photo);
        mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.buon);

    }

    // camera mặc định của máy thông qua Intent
    public void openCameraIntent(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // kiểm tra tính khả dụng của camera trong máy
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, 999);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 999 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            photo.setImageBitmap(imageBitmap);
        }
    }

    // phát nhạc từ link mp3 hoặc file offline trong project
    public void playMedia(View view) {

        String link = "https://data25.chiasenhac.com/downloads/2102/5/2101834/128/Buon%20Lam%20Chi%20Em%20Oi%20-%20Nguyen%20Minh%20Cuong.mp3";
        try {
            mediaPlayer.start();
            int duration = mediaPlayer.getDuration();
            int currentTime = mediaPlayer.getCurrentPosition();

        } catch (Exception e) {
            Log.e("A", e.toString());
        }

    }

    // mở kết nối xem hình ảnh truyền từ camera để chụp ảnh hoặc quay phim
    public void cameraPreview(View view) {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 888);
        } else {
            startCameraPreview();
        }
    }

    private PreviewView previewView;

    private void startCameraPreview() {

        previewView = findViewById(R.id.viewFinder);

        ListenableFuture instance = ProcessCameraProvider.getInstance(this);

        instance.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    ProcessCameraProvider cameraProvider = (ProcessCameraProvider) instance.get();

                    Preview preview = new Preview.Builder().build();
                    preview.setSurfaceProvider(previewView.createSurfaceProvider());

                    CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;

                    cameraProvider.unbindAll();

                    cameraProvider.bindToLifecycle((LifecycleOwner) MainActivity.this, cameraSelector, preview);

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }, ContextCompat.getMainExecutor(this));


    }

    public void play(View view) {

        try {
            mediaPlayer.pause();


        } catch (Exception e) {
            Log.e("A", e.toString());
        }
    }
}