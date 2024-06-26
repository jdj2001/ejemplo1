package com.example.ejemplo1;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ActivityVideo extends AppCompatActivity {
    static final int PETICION_ACCESO_CAMARA = 101;
    static final int PETICION_CAPTURA_VIDEO = 103;
    static final int CALIDAD_VIDEO = 1;

    static final int DURACION_VIDEO = 30; // Duración máxima en segundos del video

    VideoView videoView;
    Button btnStartRecording;
    Uri videoUri;
    String videoBase64;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        videoView = findViewById(R.id.videoView);
        btnStartRecording = findViewById(R.id.btnStartRecording);

        btnStartRecording.setOnClickListener(v -> permisosVideo());
    }


    private void permisosVideo() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, PETICION_ACCESO_CAMARA);
        } else {
            dispatchTakeVideoIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PETICION_ACCESO_CAMARA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakeVideoIntent();
            } else {
                Toast.makeText(getApplicationContext(), "Acceso Denegado", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Video.Media.TITLE, "New Video");
            values.put(MediaStore.Video.Media.DESCRIPTION, "From your Camera");
            videoUri = getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
            takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
            takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, CALIDAD_VIDEO);
            takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, DURACION_VIDEO);
            startActivityForResult(takeVideoIntent, PETICION_CAPTURA_VIDEO);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PETICION_CAPTURA_VIDEO && resultCode == RESULT_OK) {
            // Verificar si data es null o si data.getData() es null
            if (data != null && data.getData() != null) {
                videoUri = data.getData();
            }

            if (videoUri != null) {
                videoView.setVideoURI(videoUri);
                videoView.start();
                videoBase64 = convertVideoToBase64(videoUri);
                if (videoBase64 != null) {
                    Log.i("Video Base64", videoBase64);
                } else {
                    Log.e("Video Base64", "Error en la conversión del video a base64");
                }
            } else {
                Log.e("Video URI", "Error al obtener la URI del video");
            }
        }
    }

    private String convertVideoToBase64(Uri videoUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(videoUri);
            byte[] bytes = getBytes(inputStream);
            return Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
}
