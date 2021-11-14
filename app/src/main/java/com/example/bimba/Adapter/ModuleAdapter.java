package com.example.bimba.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bimba.Model.Module;
import com.example.bimba.Model.User;
import com.example.bimba.R;


import java.util.ArrayList;

public class ModuleAdapter extends RecyclerView.Adapter<ModuleAdapter.ModuleViewHolder> {
    private static final String TAG = "DataBarangAdapter";

    private ArrayList<Module> datalist;
    private Context context;
    private User user;

    public ModuleAdapter(Context context, ArrayList<Module> datalist, User user){
        this.context = context;
        this.datalist = datalist;
        this.user = user;
    }

    @Override
    public ModuleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.cardview_module, parent, false);
        return new ModuleAdapter.ModuleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ModuleAdapter.ModuleViewHolder holder, int position) {
        holder.tvNamaModule.setText(datalist.get(position).getNamaModule());
        if (datalist.get(position).getDrawableName() != 0) {
            holder.ivVectorModule.setImageResource(datalist.get(position).getDrawableName());
        }
        holder.cvMasterModule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, datalist.get(position).getActivity());
                intent.putExtra("USER_MODE", datalist.get(position).getMode());
                Log.d(TAG, "onClick: " + user.getIdUser());
                intent.putExtra("USER_ID", user.getIdUser());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: " + datalist.size());
        return (datalist != null) ? datalist.size() : 0;
    }

    public class ModuleViewHolder extends RecyclerView.ViewHolder{
        private TextView tvNamaModule;
        private ImageView ivVectorModule;
        private CardView cvMasterModule;
        public ModuleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNamaModule = (TextView) itemView.findViewById(R.id.tv_nama_module);
            ivVectorModule = (ImageView) itemView.findViewById(R.id.vector_module);
            cvMasterModule = (CardView) itemView.findViewById(R.id.cv_modul);
        }
    }
}
