package com.example.bimba.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bimba.Model.JenisPaket;
import com.example.bimba.PaketActivity;
import com.example.bimba.R;

import java.util.ArrayList;

public class ListPaketAdapter extends RecyclerView.Adapter<ListPaketAdapter.MyViewHolder>{

    private Context context;
    private ArrayList<JenisPaket> data;
    private PaketActivity.AdapterClickedListener listener;

    public ListPaketAdapter(Context cont, ArrayList<JenisPaket> data, PaketActivity.AdapterClickedListener listener){
        context= cont;
        this.data= data;
        this.listener = listener;
    }

    @Override
    public ListPaketAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_paket,parent, false);
        return new ListPaketAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListPaketAdapter.MyViewHolder holder, int position) {
        holder.namaPaket.setText(data.get(position).getDescPaket());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClickListener(data.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView namaPaket;
        LinearLayout layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            namaPaket = itemView.findViewById(R.id. tv_list_nama_paket);
            layout = itemView.findViewById(R.id.layout_list_paket);

        }
    }
}
