package com.example.bimba.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bimba.DetailPaketActivity;
import com.example.bimba.Model.JenisPaket;
import com.example.bimba.Model.ListBiaya;
import com.example.bimba.PaketActivity;
import com.example.bimba.R;

import java.util.ArrayList;

public class ListBiayaDetailPaketAdapter extends RecyclerView.Adapter<ListBiayaDetailPaketAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<ListBiaya> data;
    private DetailPaketActivity.DetailPaketListener listener;

    public ListBiayaDetailPaketAdapter(Context cont, ArrayList<ListBiaya> data, DetailPaketActivity.DetailPaketListener listener){
        context= cont;
        this.data= data;
        this.listener = listener;
    }

    @Override
    public ListBiayaDetailPaketAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_detail_biaya_paket,parent, false);
        return new ListBiayaDetailPaketAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListBiayaDetailPaketAdapter.MyViewHolder holder, int position) {
        holder.namaBiaya.setText(data.get(position).getDescBiaya());
        holder.harga.setText("Rp. " + String.valueOf(data.get(position).getHargaBiaya()));
        Log.d("tes", "onBindViewHolder: "+ data.get(position).getPembayaranBerkala());
        String pembayaran = data.get(position).getPembayaranBerkala() == 0 ? "Sekali" : "Perbulan";
        holder.jenisPembayaran.setText(pembayaran);
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDeleteListBiaya(data.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView namaBiaya, harga, jenisPembayaran;
        ImageButton btnDelete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            namaBiaya = itemView.findViewById(R.id. tv_list_nama_biaya);
            harga = itemView.findViewById(R.id.tv_list_harga_biaya);
            jenisPembayaran = itemView.findViewById(R.id.tv_list_jenis_pembayaran);
            btnDelete = itemView.findViewById(R.id.btnDeleteListBiaya);
        }
    }
}
