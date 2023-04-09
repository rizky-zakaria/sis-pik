package com.example.sis_pikmobile.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.sis_pikmobile.R;
import com.example.sis_pikmobile.api.ApiInterfaces;
import com.example.sis_pikmobile.api.ApiServer;
import com.example.sis_pikmobile.model.ResponseData;
import com.example.sis_pikmobile.utils.SharedPrefManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormActivity extends AppCompatActivity {

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    EditText pj, kegiatan, lokasi, tgl_mulai, tgl_selesai, waktu;
    String Pj, Kegiatan, Lokasi, TglMulai, TglSelesai, Waktu, Id;
    AppCompatButton btnNext;
    SharedPrefManager sharedPrefManager;
    private TimePickerDialog timePickerDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        sharedPrefManager = new SharedPrefManager(getApplicationContext());
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        pj = findViewById(R.id.pj);
        kegiatan = findViewById(R.id.kegiatan);
        lokasi = findViewById(R.id.lokasi);
        tgl_mulai = findViewById(R.id.tgl_mulai);
        tgl_selesai = findViewById(R.id.tgl_selesai);
        waktu = findViewById(R.id.waktu);
        btnNext = findViewById(R.id.btnNext);

        tgl_mulai.setOnClickListener(view -> {
            showDateDialogMulai();
        });

        tgl_selesai.setOnClickListener(view -> {
            showDateDialogSelesai();
        });

        waktu.setOnClickListener(view -> {
            showTimeDialog();
        });

        btnNext.setOnClickListener(view -> {
            Pj = pj.getText().toString();
            Kegiatan = kegiatan.getText().toString();
            Lokasi = lokasi.getText().toString();
            TglMulai = tgl_mulai.getText().toString();
            TglSelesai = tgl_selesai.getText().toString();
            Waktu = waktu.getText().toString();
            Id = sharedPrefManager.getSpId();
            postPermohonan(Pj, Kegiatan, Lokasi, TglMulai, TglSelesai, Waktu, Id);

        });

    }

    private void showTimeDialog() {
        Calendar calendar = Calendar.getInstance();
        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                waktu.setText(hourOfDay+":"+minute);
            }
        },
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                DateFormat.is24HourFormat(this));
        timePickerDialog.show();
    }

    private void showDateDialogMulai() {
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                tgl_mulai.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    private void showDateDialogSelesai(){
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                tgl_selesai.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    private void postPermohonan(String pj, String kegiatan, String lokasi, String tglMulai, String tglSelesai, String waktu, String id) {
        ApiInterfaces apiInterfaces = ApiServer.konekRetrofit().create(ApiInterfaces.class);
        Log.d("TAG", "postPermohonan: "+pj+ kegiatan+ lokasi+ tglMulai+ tglSelesai+ waktu+ id);
        Call<ResponseData> call = apiInterfaces.postDataPermohonan(pj, kegiatan, lokasi, tglMulai, tglSelesai, waktu, id);
        call.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                if (response.isSuccessful()){
                    Intent intent = new Intent(getApplicationContext(), FormKtpActivity.class);
                    intent.putExtra("ID", response.body().getData().get(0).getId().toString());
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(), "Gagal Melakukan Transaksi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}