package com.example.bimba.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bimba.Model.Siswa;
import com.example.bimba.ProfileSiswaActivity;
import com.example.bimba.R;

import java.util.ArrayList;

import static com.example.bimba.Util.loadImage;
public class DataSiswaAdapter extends RecyclerView.Adapter<DataSiswaAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Siswa> data;

    public DataSiswaAdapter(Context cont, ArrayList<Siswa> data){
        context= cont;
        this.data= data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_user,parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.vnoregis.setText("NIS : "+Integer.toString(data.get(position).getNis()));
        holder.vnama.setText(data.get(position).getFirstName()+" "+ data.get(position).getLastName());
        loadImage(data.get(position).getFotoSiswa(), holder.vFotoSiswa, context);
        if(data.get(position).getJenisKelamin().toLowerCase().equals("perempuan")){
            holder.template.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPink));
        }
        holder.template.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProfileSiswaActivity.class);
                intent.putExtra("SISWA_EXTRA",data.get(position));
                context.startActivity(intent);
                ((Activity) context).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView vnoregis, vnama;
        ImageView vFotoSiswa;
        RelativeLayout template;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            vnoregis = itemView.findViewById(R.id.tv_list_noregis);
            vnama = itemView.findViewById(R.id.tv_nama);
            vFotoSiswa = itemView.findViewById(R.id.imageViewFoto);
            template = itemView.findViewById(R.id.constraintLayout);
        }
    }
}

