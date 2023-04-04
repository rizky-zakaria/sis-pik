package com.example.sis_pikmobile.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sis_pikmobile.R;
import com.example.sis_pikmobile.api.ApiInterfaces;
import com.example.sis_pikmobile.api.ApiServer;
import com.example.sis_pikmobile.model.ResponseData;
import com.example.sis_pikmobile.utils.SharedPrefManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormKartuActivity extends AppCompatActivity {

    private static final int REQUEST_PICK_PHOTO = 2;
    private static final int REQUEST_WRITE_PERMISSION = 786;
    private String mediaPath;
    private String postPath;
    private Bitmap bitmap;
    SharedPrefManager sharedPrefManager;
    String idPermohonan;
    ImageView img;
    AppCompatButton btnNext;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            insertData(idPermohonan);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_kartu);

        Bundle bundle = getIntent().getExtras();
        idPermohonan = bundle.getString("ID");

        img = findViewById(R.id.img);
        btnNext = findViewById(R.id.btnNext);
        img.setOnClickListener(view -> {
            Intent galeri = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galeri, REQUEST_PICK_PHOTO);
        });

        btnNext.setOnClickListener(view -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
            } else {
                insertData(idPermohonan);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            if (requestCode == REQUEST_PICK_PHOTO) {
                if (data != null) {
                    // Ambil Image Dari Galeri dan Foto
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

//                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null );
                    assert cursor != null;
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    mediaPath = cursor.getString(columnIndex);
                    img.setImageURI(data.getData());
                    cursor.close();
                    postPath = mediaPath;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }


    private void insertData(String idPermohonan) {
        if (mediaPath == null){
            Toast.makeText(getApplicationContext(), "Silahkan pilih gambar terlebih dahulu", Toast.LENGTH_SHORT).show();
        }else {
            File imageFIle = new File(mediaPath);
            ApiInterfaces interfaces = ApiServer.konekRetrofit().create(ApiInterfaces.class);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 75, byteArrayOutputStream);
            byte[] imageToByte = byteArrayOutputStream.toByteArray();
            String encodeImage = android.util.Base64.encodeToString(imageToByte, Base64.DEFAULT);
            Call<ResponseData> call = interfaces.postDataKk(idPermohonan, encodeImage);
            call.enqueue(new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    if (response.isSuccessful()){
                        Intent intent = new Intent(getApplicationContext(), FormSuratActivity.class);
                        intent.putExtra("ID", idPermohonan);
                        startActivity(intent);
                    }else {
                        Toast.makeText(getApplicationContext(), "Gagal Mengirim data", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseData> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }


    }
}