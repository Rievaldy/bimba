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
import com.example.bimba.Model.ListBiaya;
import com.example.bimba.R;
import com.example.bimba.RESTAPI.Tunggakan.CompleteTunggakan;
import com.example.bimba.TagihanActivity;

import java.util.ArrayList;

public class ListTagihanAdapter extends RecyclerView.Adapter<ListTagihanAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<CompleteTunggakan> data;
    private TagihanActivity.TagihanListener listener;

    public ListTagihanAdapter(Context cont, ArrayList<CompleteTunggakan> data, TagihanActivity.TagihanListener listener){
        context= cont;
        this.data= data;
        this.listener = listener;
    }

    @Override
    public ListTagihanAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_tagihan,parent, false);
        return new ListTagihanAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListTagihanAdapter.MyViewHolder holder, int position) {
        holder.idTagihan.setText(String.valueOf(data.get(position).getTunggakan().getIdTunggakan()));
        holder.namaSiswa.setText(data.get(position).getSiswa().getFirstName() + " "+data.get(position).getSiswa().getLastName());
        holder.namaPaket.setText(data.get(position).getJenisPaket().getDescPaket());
        String status = (data.get(position).getTunggakan().getTotalHarusBayar() - data.get(position).getTunggakan().getBaruDibayarkan()) == 0 ? "Lunas" : "Belum Lunas";
        holder.statusPembayaran.setText(status);
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
        TextView idTagihan, namaSiswa,namaPaket,statusPembayaran;
        LinearLayout layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            idTagihan = itemView.findViewById(R.id.et_id_tagihan_lt);
            namaSiswa = itemView.findViewById(R.id.et_nama_siswa_lt);
            namaPaket=  itemView.findViewById(R.id.et_nama_paket_lt);
            statusPembayaran = itemView.findViewById(R.id.et_status_pembayaran_lt);
            layout = itemView.findViewById(R.id.layout_list_tagihan);
        }
    }
}
