package com.example.sis_pikmobile.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sis_pikmobile.FormPetugasActivity;
import com.example.sis_pikmobile.R;
import com.example.sis_pikmobile.api.ApiInterfaces;
import com.example.sis_pikmobile.api.ApiServer;
import com.example.sis_pikmobile.model.DataModel;
import com.example.sis_pikmobile.model.ResponseData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PetugasAdapter extends RecyclerView.Adapter<PetugasAdapter.HolderData> {

    Context context;
    List<DataModel> dataModels;

    public PetugasAdapter(Context context, List<DataModel> dataModels) {
        this.context = context;
        this.dataModels = dataModels;
    }

    @NonNull
    @Override
    public PetugasAdapter.HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_petugas, parent, false);
        HolderData holderData = new HolderData(layout);
        return holderData;
    }

    @Override
    public void onBindViewHolder(@NonNull PetugasAdapter.HolderData holder, int position) {
        DataModel dataModel = dataModels.get(position);
        holder.tvKegiatan.setText(dataModel.getKegiatan());
        holder.tvTgl.setText(dataModel.getTgl_mulai()+" - "+dataModel.getTgl_selesai()+" / "+dataModel.getWaktu());
        holder.tvStatus.setText(dataModel.getStatus());
        holder.itemView.setOnClickListener(view -> {
            if (dataModel.getStatus().equals("diajukan")){
//                Toast.makeText(context.getApplicationContext(), dataModel.getId().toString(), Toast.LENGTH_SHORT).show();
                Context context = view.getContext();
                Intent intent = new Intent(context, FormPetugasActivity.class);
                intent.putExtra("ID", dataModel.getId().toString());
                context.startActivity(intent);
            }else {
                Toast.makeText(context.getApplicationContext(), "Tidak dapat memverifikasi tempat", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataModels.size();
    }

    public class HolderData extends RecyclerView.ViewHolder {
        TextView tvKegiatan, tvTgl, tvStatus;
        ImageView img;
        public HolderData(@NonNull View itemView) {
            super(itemView);

            tvKegiatan = itemView.findViewById(R.id.kegiatan);
            tvTgl = itemView.findViewById(R.id.tgl);
            tvStatus = itemView.findViewById(R.id.status);
            img = itemView.findViewById(R.id.image);

        }
    }

    private void tolakKonfirmasi(Integer id) {
        ApiInterfaces apiInterfaces = ApiServer.konekRetrofit().create(ApiInterfaces.class);
        Call<ResponseData> call = apiInterfaces.tolakKonfirmasi(String.valueOf(id));
        call.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {

            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                Toast.makeText(context.getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void konfirmasiTempat(Integer id) {
        ApiInterfaces apiInterfaces = ApiServer.konekRetrofit().create(ApiInterfaces.class);
        Call<ResponseData> call = apiInterfaces.dataPermohonan(String.valueOf(id));
        call.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                if (response.isSuccessful()){
                    Toast.makeText(context.getApplicationContext(), "Berhasil melakukan konfirmasi", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context.getApplicationContext(), "Gagal melakukan konfirmasi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                Toast.makeText(context.getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
