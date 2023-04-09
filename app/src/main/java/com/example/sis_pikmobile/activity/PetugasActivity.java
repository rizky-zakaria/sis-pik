package com.example.sis_pikmobile.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sis_pikmobile.R;
import com.example.sis_pikmobile.adapter.PermohonanAdapter;
import com.example.sis_pikmobile.adapter.PetugasAdapter;
import com.example.sis_pikmobile.api.ApiInterfaces;
import com.example.sis_pikmobile.api.ApiServer;
import com.example.sis_pikmobile.model.DataModel;
import com.example.sis_pikmobile.model.ResponseData;
import com.example.sis_pikmobile.utils.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PetugasActivity extends AppCompatActivity {

    RecyclerView list_item;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<DataModel> dataModels = new ArrayList<>();
    ImageButton btnAdd;
    LinearLayout btnLogout;
    SharedPrefManager sharedPrefManager;
    TextView nama, email, kosong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petugas);

        sharedPrefManager = new SharedPrefManager(getApplicationContext());

        kosong = findViewById(R.id.kosong);

        list_item = findViewById(R.id.list_item);
        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        list_item.setLayoutManager(layoutManager);
        btnLogout = findViewById(R.id.btnLogout);

        nama = findViewById(R.id.nama);
        email = findViewById(R.id.email);

        nama.setText(sharedPrefManager.getSPNama());
        email.setText(sharedPrefManager.getSPEmail());

        btnLogout.setOnClickListener(view -> {
            logout();
        });

        getData(sharedPrefManager.getSpId());
    }

    private void logout() {
        sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN, false);
        startActivity(new Intent(getApplicationContext(), LoginActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
    }

    private void getData(String spId) {
        ApiInterfaces apiInterfaces = ApiServer.konekRetrofit().create(ApiInterfaces.class);
        Call<ResponseData> call = apiInterfaces.dataPermohonanPetugas(spId);
        call.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                if (response.isSuccessful()){
                    dataModels = response.body().getData();
                    adapter = new PetugasAdapter(getApplicationContext(), dataModels);
                    list_item.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }else {
                    kosong.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Anda tidak memiliki data permohonan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}