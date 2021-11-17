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
import com.example.bimba.ProfileSiswaActivity;
import com.example.bimba.R;
import com.example.bimba.RESTAPI.HistoryPembayaran.CompleteHistoryPembayaran;
import com.example.bimba.RESTAPI.Tunggakan.CompleteTunggakan;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ListPaketSiswaAdapter extends RecyclerView.Adapter<ListPaketSiswaAdapter.MyViewHolder>{

    private Context context;
    private ArrayList<CompleteTunggakan> data;
    private ProfileSiswaActivity.ProfileSiswaListener listener;

    public ListPaketSiswaAdapter(Context cont, ArrayList<CompleteTunggakan> data, ProfileSiswaActivity.ProfileSiswaListener listener){
        context= cont;
        this.data= data;
        this.listener = listener;
    }

    @Override
    public ListPaketSiswaAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_paket_siswa,parent, false);
        return new ListPaketSiswaAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListPaketSiswaAdapter.MyViewHolder holder, int position) {
        holder.namaPaket.setText(data.get(position).getJenisPaket().getDescPaket());
        holder.tanggalAwal.setText(data.get(position).getTunggakan().getTahunMasuk());
        //getting last date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(data.get(position).getTunggakan().getTahunMasuk()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.MONTH, data.get(position).getJenisPaket().getMasaPembelajaran());
        String lastdate = sdf.format(c.getTime());
        holder.tanggalAkhir.setText(lastdate);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClickPaket(data.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView namaPaket, tanggalAwal, tanggalAkhir;
        LinearLayout layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            namaPaket = itemView.findViewById(R.id. et_nama_paket_lps);
            tanggalAwal = itemView.findViewById(R.id.et_tanggal_awal_lts);
            tanggalAkhir = itemView.findViewById(R.id.et_tanggal_selesai_lts);
            layout = itemView.findViewById(R.id.layout_list_paket_siswa);

        }
    }
}
