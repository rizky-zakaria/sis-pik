package com.example.sis_pikmobile;

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
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sis_pikmobile.activity.PetugasActivity;
import com.example.sis_pikmobile.api.ApiInterfaces;
import com.example.sis_pikmobile.api.ApiServer;
import com.example.sis_pikmobile.model.ResponseVerifikasi;
import com.example.sis_pikmobile.utils.SharedPrefManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormPetugasActivity extends AppCompatActivity {

    EditText keterangan;
    private static final int REQUEST_PICK_PHOTO = 2;
    private static final int REQUEST_WRITE_PERMISSION = 786;
    private String mediaPath;
    private String postPath;
    private Bitmap bitmap;
    SharedPrefManager sharedPrefManager;
    String idPermohonan, sKeterangan, sKet;
    ImageView img;
    AppCompatButton btnNext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_petugas);

        Bundle bundle = getIntent().getExtras();
        idPermohonan = bundle.getString("ID");
        keterangan = findViewById(R.id.keterangan);
        sKeterangan = keterangan.getText().toString();
//        Log.d("TAG", "onCreate: "+idPermohonan);
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
                sKeterangan = keterangan.getText().toString();
                insertData();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            insertData();
        }
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


    private void insertData() {
        if (mediaPath == null){
            Toast.makeText(getApplicationContext(), "Silahkan pilih gambar terlebih dahulu", Toast.LENGTH_SHORT).show();
        }else {
            File imageFIle = new File(mediaPath);
            Toast.makeText(getApplicationContext(), idPermohonan, Toast.LENGTH_SHORT).show();
            ApiInterfaces interfaces = ApiServer.konekRetrofit().create(ApiInterfaces.class);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 75, byteArrayOutputStream);
            byte[] imageToByte = byteArrayOutputStream.toByteArray();
            String encodeImage = android.util.Base64.encodeToString(imageToByte, Base64.DEFAULT);
            sKet = keterangan.getText().toString();
            Bundle bundle = getIntent().getExtras();
            String id = bundle.getString("ID");
//            Log.d("TAG", "insertData: "+idPermohonan+" "+sKet+ " " +encodeImage);
            Call<ResponseVerifikasi> call = interfaces.konfirmasiTempat(idPermohonan, encodeImage, sKet);
            call.enqueue(new Callback<ResponseVerifikasi>() {
                @Override
                public void onResponse(Call<ResponseVerifikasi> call, Response<ResponseVerifikasi> response) {
                    if (response.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "Berhasil melakukan verifikasi", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), PetugasActivity.class));
                        finish();
                    }else {
                        Toast.makeText(getApplicationContext(), "Gagal melakukan verifikasi", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), PetugasActivity.class));
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<ResponseVerifikasi> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}