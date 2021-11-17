package com.example.bimba.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bimba.R;
import com.example.bimba.RESTAPI.HistoryPembayaran.CompleteHistoryPembayaran;
import com.example.bimba.RESTAPI.Tunggakan.CompleteTunggakan;
import com.example.bimba.SessionManagement;
import com.example.bimba.TagihanActivity;
import com.example.bimba.TransaksiActivity;

import java.util.ArrayList;

public class ListHistoryPembayaranAdapter extends RecyclerView.Adapter<ListHistoryPembayaranAdapter.MyViewHolder>  {

    private Context context;
    private ArrayList<CompleteHistoryPembayaran> data;
    private TransaksiActivity.HistoryPembayaranListener listener;
    private SessionManagement sessionManagement;

    public ListHistoryPembayaranAdapter(Context cont, ArrayList<CompleteHistoryPembayaran> data, TransaksiActivity.HistoryPembayaranListener listener){
        context= cont;
        this.data= data;
        this.listener = listener;
        sessionManagement = new SessionManagement(context.getApplicationContext());
    }

    @Override
    public ListHistoryPembayaranAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_transaksi,parent, false);
        return new ListHistoryPembayaranAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListHistoryPembayaranAdapter.MyViewHolder holder, int position) {
        holder.idHistoryPembayaran.setText(String.valueOf(data.get(position).getHistoryPembayaran().getIdHistoryPembayaran()));
        holder.namaSiswa.setText(data.get(position).getSiswa().getFirstName() + " "+data.get(position).getSiswa().getLastName());
        holder.namaPaket.setText(data.get(position).getJenisPaket().getDescPaket());
        String status = (data.get(position).getTunggakan().getTotalHarusBayar() - data.get(position).getTunggakan().getBaruDibayarkan()) == 0 ? "Lunas" : "Belum Lunas";
        holder.namaUser.setText(data.get(position).getUser().getFirstName() + " "+data.get(position).getUser().getLastName());
        if(sessionManagement.getUserAccessSession() == 1){
            if(data.get(position).getHistoryPembayaran().getApproved() == 0){
                holder.layout.setBackgroundColor(ContextCompat.getColor(context, R.color.colorRed));
            }else{
                holder.layout.setBackgroundColor(ContextCompat.getColor(context, R.color.colorGreen));
            }
        }

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClickListener(data.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView idHistoryPembayaran, namaSiswa,namaPaket,namaUser;
        LinearLayout layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            idHistoryPembayaran = itemView.findViewById(R.id.et_id_history_pembayaran_ltr);
            namaSiswa = itemView.findViewById(R.id.et_nama_siswa_ltr);
            namaPaket=  itemView.findViewById(R.id.et_nama_paket_ltr);
            namaUser = itemView.findViewById(R.id.et_nama_user_ltr);
            layout = itemView.findViewById(R.id.layout_list_transaksi);
        }
    }

}
