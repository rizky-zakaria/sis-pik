package com.example.sis_pikmobile.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sis_pikmobile.R;
import com.example.sis_pikmobile.api.ApiInterfaces;
import com.example.sis_pikmobile.api.ApiServer;
import com.example.sis_pikmobile.model.ResponseUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    EditText email, password, name;
    String sEmail, sPassword, sName;
    AppCompatButton btnRegister;
    TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        tvLogin = findViewById(R.id.login);
        tvLogin.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        });

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        name = findViewById(R.id.name);

        btnRegister = findViewById(R.id.register);
        btnRegister.setOnClickListener(view -> {
            sEmail = email.getText().toString();
            sPassword = password.getText().toString();
            sName = name.getText().toString();
//            Log.d("TAG", "onCreate: "+sEmail + sPassword + sName);
            register(sEmail, sPassword, sName);
        });

    }

    private void register(String sEmail, String sPassword, String sName) {
        ApiInterfaces apiInterfaces = ApiServer.konekRetrofit().create(ApiInterfaces.class);
        Call<ResponseUser> call = apiInterfaces.dataRegister(sName, sEmail, sPassword, sPassword);
        call.enqueue(new Callback<ResponseUser>() {
            @Override
            public void onResponse(Call<ResponseUser> call, Response<ResponseUser> response) {
                Log.d("TAG", "onResponse: "+response.body().getSuccess());
                if (response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Berhasil melakukan Registrasi", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                }else {
                    Toast.makeText(getApplicationContext(), "Gagal melakukan registrasi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseUser> call, Throwable t) {

            }
        });
    }
}