package com.example.sis_pikmobile.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sis_pikmobile.R;
import com.example.sis_pikmobile.api.ApiInterfaces;
import com.example.sis_pikmobile.api.ApiServer;
import com.example.sis_pikmobile.model.DataModel;
import com.example.sis_pikmobile.model.ResponseFile;
import com.example.sis_pikmobile.utils.Config;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PermohonanAdapter extends RecyclerView.Adapter<PermohonanAdapter.HolderData> {

    Context context;
    List<DataModel> dataModels;

    public PermohonanAdapter(Context context, List<DataModel> dataModels) {
        this.context = context;
        this.dataModels = dataModels;
    }

    @NonNull
    @Override
    public PermohonanAdapter.HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        HolderData holderData = new HolderData(layout);
        return holderData;
    }

    @Override
    public void onBindViewHolder(@NonNull PermohonanAdapter.HolderData holder, int position) {
        DataModel dataModel = dataModels.get(position);
        holder.tvKegiatan.setText(dataModel.getKegiatan());
        holder.tvTgl.setText(dataModel.getTgl_mulai()+" - "+dataModel.getTgl_selesai()+" / "+dataModel.getWaktu());
        holder.tvStatus.setText(dataModel.getStatus());
        holder.itemView.setOnClickListener(view -> {
            if (dataModel.getStatus().equals("disetujui")){
                getFile(dataModels.get(position).getId(), view);
            }else {
                Toast.makeText(view.getContext(), "Permohonan belum disetujui", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getFile(Integer id, View view) {
        ApiInterfaces apiInterfaces = ApiServer.konekRetrofit().create(ApiInterfaces.class);
        Call<ResponseFile> call = apiInterfaces.getFile(String.valueOf(id));
        call.enqueue(new Callback<ResponseFile>() {
            @Override
            public void onResponse(Call<ResponseFile> call, Response<ResponseFile> response) {
                if (response.isSuccessful()){
                    Context context = view.getContext();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Config.BASEURL +"uploads/img/"+response.body().getData().getHasil()));
                    context.startActivity(intent);
                }else {
                    Toast.makeText(context.getApplicationContext(), "Gagal mendapatkan data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseFile> call, Throwable t) {
                Toast.makeText(context.getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return dataModels.size();
    }

    public class HolderData extends RecyclerView.ViewHolder {
        TextView tvKegiatan, tvTgl, tvStatus;

        public HolderData(@NonNull View itemView) {
            super(itemView);

            tvKegiatan = itemView.findViewById(R.id.kegiatan);
            tvTgl = itemView.findViewById(R.id.tgl);
            tvStatus = itemView.findViewById(R.id.status);

        }
    }
}
