package com.example.bimba.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bimba.BiayaActivity;
import com.example.bimba.Model.ListBiaya;
import com.example.bimba.Model.Siswa;
import com.example.bimba.ProfileSiswaActivity;
import com.example.bimba.R;

import java.util.ArrayList;

import static com.example.bimba.Util.loadImage;

public class ListBiayaAdapter extends RecyclerView.Adapter<ListBiayaAdapter.MyViewHolder>{
    private Context context;
    private BiayaActivity.EditListener listener;
    private ArrayList<ListBiaya> data;

    public ListBiayaAdapter(Context cont, ArrayList<ListBiaya> data, BiayaActivity.EditListener listener){
        context= cont;
        this.data= data;
        this.listener = listener;
    }

    @Override
    public ListBiayaAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_biaya,parent, false);
        return new ListBiayaAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListBiayaAdapter.MyViewHolder holder, int position) {
        holder.descBiaya.setText(data.get(position).getDescBiaya());
        String harga = String.valueOf(data.get(position).getHargaBiaya());
        holder.harga.setText("Rp. "+ harga);
        holder.editBiaya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onEditClicker(data.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView editBiaya;
        TextView descBiaya;
        TextView harga;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            editBiaya = itemView.findViewById(R.id.iv_edit_list_biaya);
            descBiaya = itemView.findViewById(R.id.tv_list_desc_biaya);
            harga = itemView.findViewById(R.id.tv_list_harga_biaya);

        }
    }
}
